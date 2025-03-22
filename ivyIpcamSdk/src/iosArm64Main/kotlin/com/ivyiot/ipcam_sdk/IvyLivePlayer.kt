package com.ivyiot.ipcam_sdk

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun IvyLivePlayer(
    ivyCameraConnection: IvyCameraConnection,
    scale: Float,
    offset: Offset,
    modifier: Modifier
) {
    val image by ivyCameraConnection.liveStreamImageFlow.collectAsStateWithLifecycle(null)

    image?.let {
        Image(
            it,
            contentDescription = null,
            modifier = modifier.graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
        )
    }

    DisposableEffect(Unit) {
        ivyCameraConnection.playLiveStream()

        onDispose {
            ivyCameraConnection.stopLiveStream()
        }
    }
}
