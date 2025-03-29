package com.ivyiot.ipcam_sdk

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer

@Composable
actual fun IvyLivePlayer(
    ivyCameraConnection: IvyCameraConnection,
    scale: Float,
    offset: Offset,
    modifier: Modifier
) {
    val image by remember { ivyCameraConnection.liveStreamImages }

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
