package com.ivyiot.ipcam_sdk

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.viewinterop.AndroidView
import com.ivyiot.ipclibrary.video.IVideoListener
import com.ivyiot.ipclibrary.video.VideoSurfaceView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
actual fun IvyLivePlayer(
    ivyCameraConnection: IvyCameraConnection,
    scale: Float,
    offset: Offset,
    modifier: Modifier
) {
    var isInitialised by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    AndroidView(
        factory = { context ->
            VideoSurfaceView(context).also {
                it.clipToOutline = true

                coroutineScope.launch {
                    while(true) {
                        delay(1.seconds)
                        ivyCameraConnection.setFlowSpeed(it.currFlowValue)
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

                it.openVideo((ivyCameraConnection as IvyCameraConnectionImpl).ivyCamera, object : IVideoListener {
                    override fun snapFinished(p0: ByteArray?) {
                        TODO("Not yet implemented")
                    }

                    override fun firstFrameDone(p0: Bitmap?) {
                        println("First frame done")
                    }

                    override fun openVideoSucc() {
                        println("Video open success")
                    }

                    override fun openVideoFail(p0: Int) {
                        println("Video open error: $p0")
                    }

                    override fun closeVideoSucc() {
                        println("Close video success")
                    }

                    override fun closeVideoFail(p0: Int) {
                        println("Close video failure")
                    }

                    override fun netFlowSpeedRefresh(p0: String?) {
                    }

                })
            }
        },
        onRelease = {
            it.closeVideo()
            it.clearVideoView()
        }
    )
}
