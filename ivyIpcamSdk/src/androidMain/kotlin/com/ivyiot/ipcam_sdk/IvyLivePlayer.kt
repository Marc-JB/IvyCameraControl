package com.ivyiot.ipcam_sdk

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
import com.ivyiot.ipcam_sdk.models.Bitrate
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
                    try {
                        while(true) {
                            delay(1.seconds)
                            ivyCameraConnection.setFlowSpeed(Bitrate(it.currFlowValue.toUInt()))
                        }
                    } finally {
                        ivyCameraConnection.setFlowSpeed(null)
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

                it.openVideo(ivyCameraConnection.ivyCamera, ivyCameraConnection.videoListener)
            }
        },
        onRelease = {
            it.closeVideo()
            it.clearVideoView()
        }
    )
}
