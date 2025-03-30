package com.ivyiot.ipcam_sdk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.graphics.SurfaceTexture
import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Handler
import android.os.SystemClock
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import com.ivyio.sdk.FrameData
import com.ivyio.sdk.IvyIoInteger
import com.ivyio.sdk.IvyIoSdkJni
import com.ivyio.sdk.OpenVideoArgsType0
import com.ivyiot.ipcam_sdk.utils.BitrateHandler
import com.ivyiot.ipcam_sdk.utils.HardwareDecodeBufferException
import com.ivyiot.ipcam_sdk.utils.HardwareDecodeUtils.deviceDoesNotSupportHardwareDecode
import com.ivyiot.ipclibrary.common.Global
import com.ivyiot.ipclibrary.model.IvyCamera
import com.ivyiot.ipclibrary.util.CommonUtil
import com.ivyiot.ipclibrary.video.IVideoListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.ByteBuffer
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration.Companion.seconds

class IvyVideoSurfaceView : TextureView, SurfaceTextureListener {
    private var mSurface: Surface? = null

    private var hardwareDecodingThread: HardwareDecodingThread? = null
    private var softwareDecodingThread: SoftwareDecodingThread? = null

    var ivyCamera: IvyCamera? = null
    private val ivyCameraHandle: Int
        get() = ivyCamera?.handle ?: 0

    var hasFirstFrameBeenDrawnToScreen: Boolean = true
    private val destinationSurfaceDimensions = Rect()
    private var softwareRenderingBitmap: Bitmap? = null
    val bitrateHandler: BitrateHandler = BitrateHandler()
    private var hardwareDecodeFrameData = FrameData()
    var videoListener: IVideoListener? = null
    private var bitrateResetScope: CoroutineScope = CoroutineScope(EmptyCoroutineContext)

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : super(context, attrs, defStyle)

    init {
        surfaceTextureListener = this
        updateSurfaceDimensions(width, height)
    }

    fun openVideo() {
        this.startDecodingThread()
    }

    fun closeVideo() {
        this.stopDraw()
    }

    suspend fun snap(imgPath: String?): Boolean {
        val snapPictureBitmap = this@IvyVideoSurfaceView.bitmapFromStream
        return if (snapPictureBitmap != null) {
            withContext(Dispatchers.IO) {
                CommonUtil.saveBmp2file(snapPictureBitmap, imgPath)
            }
            true
        } else {
            val result = withContext(Dispatchers.IO) {
                IvyIoSdkJni.netSnapPicture(
                    this@IvyVideoSurfaceView.ivyCameraHandle,
                    imgPath,
                    30000
                )
            }
            result == 0
        }
    }

    private val imageBytesFromHardwareDecodedStream: ByteArray?
        get() {
            val snapFrameData = this.hardwareDecodeFrameData
            if (snapFrameData.data == null || snapFrameData.data.size == 0) {
                return null
            }

            val data = ByteArray(1048576)
            val dataLen = IvyIoInteger(0)
            val result = IvyIoSdkJni.keyFrame2Picture(
                this.ivyCameraHandle,
                snapFrameData.data,
                data,
                dataLen,
                0
            )
            return if (result == 0 && dataLen.intValue() > 0) data.copyOfRange(0, dataLen.intValue()) else null
        }

    private val bitmapFromHardwareDecodedStream: Bitmap?
        get() {
            val imageBytes = this.imageBytesFromHardwareDecodedStream
            return if (imageBytes == null) null else BitmapFactory.decodeByteArray(
                imageBytes,
                0,
                imageBytes.size
            )
        }

    private val bitmapFromStream: Bitmap?
        get() {
            val softwareRenderingBitmap =
                this@IvyVideoSurfaceView.softwareRenderingBitmap
            return softwareRenderingBitmap ?: this.bitmapFromHardwareDecodedStream
        }

    fun startDecodingThread() {
        isDraw = true
        if (!deviceDoesNotSupportHardwareDecode) {
            if (this.hardwareDecodingThread == null) {
                this.hardwareDecodingThread = HardwareDecodingThread()
                this.hardwareDecodingThread!!.start()
            }
        } else {
            startSoftwareDecodeThread()
        }
    }

    private fun startSoftwareDecodeThread() {
        if (this.softwareDecodingThread == null) {
            this.softwareDecodingThread = SoftwareDecodingThread()
            this.softwareDecodingThread!!.start()
        }
    }

    private fun stopDraw() {
        isDraw = false

        if (this.softwareDecodingThread != null) {
            try {
                this.softwareDecodingThread!!.join()
            } catch (ignored: InterruptedException) {
            }
        }
        this.softwareDecodingThread = null

        if (null != this.softwareRenderingBitmap && !this.softwareRenderingBitmap!!.isRecycled) {
            this.softwareRenderingBitmap!!.recycle()
        }
        this.softwareRenderingBitmap = null

        if (null != this.hardwareDecodingThread) {
            try {
                this.hardwareDecodingThread!!.join()
            } catch (ignored: InterruptedException) {
            }
        }
        this.hardwareDecodingThread = null

        this.hasFirstFrameBeenDrawnToScreen = false

        bitrateResetScope.cancel()
        bitrateResetScope = CoroutineScope(EmptyCoroutineContext)
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        mSurface = Surface(surfaceTexture)
        updateSurfaceDimensions(width, height)
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        updateSurfaceDimensions(width, height)
    }

    private fun updateSurfaceDimensions(width: Int, height: Int) {
        this.destinationSurfaceDimensions.right = width
        this.destinationSurfaceDimensions.bottom = height
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        this.stopDraw()
        if (this.hardwareDecodingThread != null) {
            this.hardwareDecodingThread!!.interrupt()
            this.hardwareDecodingThread = null
        }

        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        this.firstFrameNotify()
    }

    private fun firstFrameNotify() {
        if (!this.hasFirstFrameBeenDrawnToScreen) {
            this.hasFirstFrameBeenDrawnToScreen = true

            bitrateResetScope.launch {
                while (isActive) {
                    bitrateHandler.resetBitrateWhenUnresponsive()
                    delay(3.seconds)
                }
            }

            if (this.videoListener != null) {
                val bitmap = this.bitmapFromStream
                if (bitmap != null) {
                    this@IvyVideoSurfaceView.videoListener?.firstFrameDone(bitmap)
                }
            }
        }
    }

    private inner class HardwareDecodingThread : Thread() {
        private var decoder: MediaCodec? = null
        var switchToSoftwareEncoder: Boolean = false

        @Throws(IOException::class)
        fun createDecoder(key_mime: String, width: Int, height: Int): MediaCodec {
            this.decoder = MediaCodec.createDecoderByType(key_mime)
            val mediaFormat = MediaFormat.createVideoFormat(key_mime, width, height)
            mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 0)
            this.decoder!!.configure(mediaFormat, this@IvyVideoSurfaceView.mSurface, null, 0)
            this.decoder!!.start()
            return this.decoder!!
        }

        fun stopAndReleaseDecoder() {
            if (this.decoder != null) {
                this.decoder!!.stop()
                this.decoder!!.release()
            }
            this.decoder = null
        }

        override fun run() {
            while (isDraw && (this@IvyVideoSurfaceView.mSurface == null || !this@IvyVideoSurfaceView.mSurface!!.isValid())) {
                SystemClock.sleep(50L)
            }

            var key_mime = MediaFormat.MIMETYPE_VIDEO_AVC

            val initialFrameData = FrameData()
            while (isDraw) {
                val flow_value = IvyIoInteger(0)
                val result = IvyIoSdkJni.getRawStreamData(
                    this@IvyVideoSurfaceView.ivyCameraHandle,
                    0,
                    initialFrameData,
                    flow_value,
                    0
                )
                if (result == 0 && initialFrameData.dataLen > 0) {
                    if (initialFrameData.fmt == 0) {
                        key_mime = MediaFormat.MIMETYPE_VIDEO_AVC
                    } else if (initialFrameData.fmt == 1) {
                        key_mime = MediaFormat.MIMETYPE_VIDEO_HEVC
                    }
                    break
                }

                SystemClock.sleep(50L)
            }

            stopAndReleaseDecoder()

            val decoder: MediaCodec

            try {
                this.switchToSoftwareEncoder = false
                decoder =
                    this.createDecoder(key_mime, initialFrameData.video_w, initialFrameData.video_h)
            } catch (e: Exception) {
                stopAndReleaseDecoder()
                startSoftwareDecodeThread()
                return
            }

            var bufferErrorCounter = 0

            while (isDraw) {
                try {
                    val inputBuffIndex = decoder.dequeueInputBuffer(0L)
                    if (inputBuffIndex == -1) {
                        if (bufferErrorCounter++ > 1000) {
                            throw HardwareDecodeBufferException()
                        }
                        continue
                    }

                    val flow_value = IvyIoInteger(0)
                    val frameData = FrameData()
                    val result = IvyIoSdkJni.getRawStreamData(
                        this@IvyVideoSurfaceView.ivyCameraHandle,
                        0,
                        frameData,
                        flow_value,
                        0
                    )

                    if (result != 0 || frameData.dataLen <= 0) {
                        SystemClock.sleep(15L)
                        continue
                    }

                    this@IvyVideoSurfaceView.bitrateHandler.insertValue(flow_value.intValue())
                    this@IvyVideoSurfaceView.hardwareDecodeFrameData = frameData

                    if (frameData.key == 1) {
                        firstFrameNotify()
                    }

                    val inputBuffer = decoder.getInputBuffer(inputBuffIndex)
                    if (inputBuffer != null) {
                        inputBuffer.clear()
                        inputBuffer.put(frameData.data, 0, frameData.dataLen)
                    }
                    decoder.queueInputBuffer(inputBuffIndex, 0, frameData.dataLen, 0L, 0)

                    val info = MediaCodec.BufferInfo()
                    val outputBufferIndex = decoder.dequeueOutputBuffer(info, 0L)
                    if (outputBufferIndex >= 0) {
                        if (info.size != 0) {
                            decoder.releaseOutputBuffer(outputBufferIndex, true)
                        } else {
                            decoder.releaseOutputBuffer(outputBufferIndex, false)
                        }

                        this@IvyVideoSurfaceView.firstFrameNotify()
                        bufferErrorCounter = 0
                    } else if (frameData.dataLen > 0) {
                        if (bufferErrorCounter++ > 1000) {
                            throw HardwareDecodeBufferException()
                        }
                    }
                } catch (e: Throwable) {
                    this.switchToSoftwareEncoder = true
                    break
                }
            }

            try {
                stopAndReleaseDecoder()
            } catch (ignored: Throwable) {
            }

            if (this.switchToSoftwareEncoder) {
                startSoftwareDecodeThread()
            }
        }
    }

    private inner class SoftwareDecodingThread : Thread() {
        private var width = 0
        private var height = 0

        override fun run() {
            while (isDraw) {
                if (this@IvyVideoSurfaceView.mSurface?.isValid != true) {
                    SystemClock.sleep(50L)
                    continue
                }

                val videoData = FrameData()
                val flow_value = IvyIoInteger(0)
                val result = IvyIoSdkJni.getStreamData(
                    this@IvyVideoSurfaceView.ivyCameraHandle,
                    0,
                    videoData,
                    flow_value,
                    2,
                    0
                )

                if (videoData.dataLen <= 0 || result != 0) {
                    SystemClock.sleep(15L)
                    continue
                }

                val canvas = this@IvyVideoSurfaceView.lockCanvas()

                if (canvas == null) {
                    continue
                }

                try {
                    this@IvyVideoSurfaceView.bitrateHandler.insertValue(flow_value.intValue())
                    val buffer = ByteBuffer.wrap(videoData.data)
                    if (buffer.capacity() <= 0 || videoData.video_w <= 0 || videoData.video_h <= 0) {
                        continue
                    }

                    var currentBitmap = this@IvyVideoSurfaceView.softwareRenderingBitmap

                    if (width != videoData.video_w || height != videoData.video_h) {
                        if (currentBitmap != null && !currentBitmap.isRecycled()) {
                            currentBitmap.recycle()
                        }
                        currentBitmap = null
                    }

                    if (currentBitmap == null) {
                        currentBitmap = Bitmap.createBitmap(
                            videoData.video_w,
                            videoData.video_h,
                            Bitmap.Config.ARGB_8888
                        )
                        this@IvyVideoSurfaceView.softwareRenderingBitmap = currentBitmap
                        width = videoData.video_w
                        height = videoData.video_h
                    }

                    currentBitmap.copyPixelsFromBuffer(buffer)
                    this@IvyVideoSurfaceView.firstFrameNotify()
                    canvas.drawBitmap(
                        currentBitmap,
                        null,
                        this@IvyVideoSurfaceView.destinationSurfaceDimensions,
                        null
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                } finally {
                    try {
                        this@IvyVideoSurfaceView.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        @Volatile
        private var isDraw = false
    }
}
