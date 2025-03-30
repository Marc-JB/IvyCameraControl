package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    )

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
