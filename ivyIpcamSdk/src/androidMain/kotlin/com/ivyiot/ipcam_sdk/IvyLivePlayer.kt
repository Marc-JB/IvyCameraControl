package com.ivyiot.ipcam_sdk

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.os.Handler
import android.os.Looper
import android.view.Surface
import androidx.compose.foundation.AndroidEmbeddedExternalSurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.viewinterop.AndroidView
import com.ivyio.sdk.FrameData
import com.ivyio.sdk.IvyIoInteger
import com.ivyio.sdk.IvyIoSdkJni
import com.ivyiot.ipcam_sdk.models.Bitrate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

private fun Surface.createMediaCodec(mimeType: String, width: Int, height: Int): MediaCodec {
    val mediaFormat = MediaFormat.createVideoFormat(mimeType, width, height)

    val mediaCodecList = MediaCodecList(MediaCodecList.REGULAR_CODECS)
    val decoderName = mediaCodecList.findDecoderForFormat(mediaFormat)

    if (decoderName == null) {
        throw RuntimeException("No media codec found for mime $mimeType");
    }

    val decoder = MediaCodec.createByCodecName(decoderName)
    decoder.configure(mediaFormat, this, null, 0)
    return decoder
}

@Composable
actual fun IvyLivePlayer(
    ivyCameraConnection: IvyCameraConnection,
    scale: Float,
    offset: Offset,
    modifier: Modifier
) {
    var isInitialised by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AndroidEmbeddedExternalSurface(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
    ) {
        onSurface { surface, surfaceWidth, surfaceHeight ->
            var width = surfaceWidth
            var height = surfaceHeight

            surface.onChanged { newSurfaceWidth, newSurfaceHeight ->
                width = newSurfaceWidth
                height = newSurfaceHeight
            }

            val initialFrameData = ivyCameraConnection.getInitialFrameData()

            val mimeType = if (initialFrameData.fmt == 1) {
                MediaFormat.MIMETYPE_VIDEO_HEVC
            } else {
                MediaFormat.MIMETYPE_VIDEO_AVC
            }

            println("Media codec: $mimeType")

            val mediaCodec = surface.createMediaCodec(mimeType, initialFrameData.video_w, initialFrameData.video_h)

            surface.onDestroyed {
                mediaCodec.stop()
                mediaCodec.release()
            }

            var hasFirstFrameBeenDrawn = false

            withContext(Dispatchers.IO) {
                mediaCodec.setCallback(object : MediaCodec.Callback() {
                    override fun onInputBufferAvailable(
                        codec: MediaCodec,
                        index: Int
                    ) {
                        val inputBuffer = codec.getInputBuffer(index) ?: return

                        val flowValue = IvyIoInteger(0)
                        val frameData = FrameData()
                        val result = IvyIoSdkJni.getRawStreamData(ivyCameraConnection.ivyCamera.handle, 0, frameData, flowValue, 0)

                        inputBuffer.clear()
                        if (result != 0 || frameData.dataLen <= 0) {
                            codec.queueInputBuffer(index, 0, 0, 0L, 0)
                        } else {
                            inputBuffer.put(frameData.data, 0, frameData.dataLen)
                            codec.queueInputBuffer(index, 0, frameData.dataLen, 0L, 0)
                        }
                    }

                    override fun onOutputBufferAvailable(
                        codec: MediaCodec,
                        index: Int,
                        info: MediaCodec.BufferInfo
                    ) {
                        if (!hasFirstFrameBeenDrawn){
                            hasFirstFrameBeenDrawn = true
                            ivyCameraConnection.videoListener.firstFrameDone(null)
                        }

                        codec.releaseOutputBuffer(index, info.size != 0)
                    }

                    override fun onError(
                        codec: MediaCodec,
                        e: MediaCodec.CodecException
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onOutputFormatChanged(
                        codec: MediaCodec,
                        format: MediaFormat
                    ) {}
                })

                mediaCodec.start()
            }



            /*while(isActive) {
                val inputBufferIndex = withContext(Dispatchers.IO) {
                    mediaCodec.dequeueInputBuffer(-1)
                }

                val flowValue = IvyIoInteger(0)
                val frameData = FrameData()
                while(isActive) {
                    val result = IvyIoSdkJni.getRawStreamData(ivyCameraConnection.ivyCamera.handle, 0, frameData, flowValue, 0)

                    if (result != 0 || frameData.dataLen <= 0) {
                        delay(15.milliseconds)
                    } else {
                        break
                    }
                }

                val inputBuffer = mediaCodec.getInputBuffer(inputBufferIndex)!!
                inputBuffer.clear()
                inputBuffer.put(frameData.data, 0, frameData.dataLen)
                mediaCodec.queueInputBuffer(inputBufferIndex, 0, frameData.dataLen, 0L, 0)

                val info = MediaCodec.BufferInfo()
                val outputBufferIndex = withContext(Dispatchers.IO) {
                    mediaCodec.dequeueOutputBuffer(info, -1)
                }

                mediaCodec.releaseOutputBuffer(outputBufferIndex, info.size != 0)
            }*/
        }
    }

    /*AndroidView(
        factory = { context ->
            IvyVideoSurfaceView(context).also {
                it.clipToOutline = true
                it.ivyCamera = ivyCameraConnection.ivyCamera
                it.videoListener = ivyCameraConnection.videoListener

                coroutineScope.launch {
                    it.bitrateHandler.bitrate.collect {
                        ivyCameraConnection.setFlowSpeed(if (it == 0) null else Bitrate(it.toUInt()))
                    }
                }
            }
        },
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            ),
        update = {
            if (!isInitialised) {
                isInitialised = true

                it.openVideo()
            }
        },
        onRelease = {
            it.closeVideo()
        }
    )*/

    DisposableEffect(Unit) {
        coroutineScope.launch {
            ivyCameraConnection.playLiveStream()
        }

        onDispose {
            GlobalScope.launch {
                ivyCameraConnection.stopLiveStream()
            }
        }
    }
}
