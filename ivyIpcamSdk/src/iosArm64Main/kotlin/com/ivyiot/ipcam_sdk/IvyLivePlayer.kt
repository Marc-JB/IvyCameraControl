package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import platform.UIKit.UIImageView

@Composable
actual fun IvyLivePlayer(ivyCameraConnection: IvyCameraConnection, modifier: Modifier) {
    val iosIvyCameraConnection = ivyCameraConnection as IvyCameraConnectionImpl
    var isInitialised by remember { mutableStateOf(false) }
    val image by ivyCameraConnection.liveStreamImageFlow.collectAsStateWithLifecycle(null)
    UIKitView(
        factory = {
            UIImageView()
        },
        modifier = modifier,
        update = {
            if (!isInitialised) {
                isInitialised = true
                iosIvyCameraConnection.playLiveStream()
            }

            it.image = image
        },
        onRelease = {
            iosIvyCameraConnection.stopLiveStream()
        }
    )
}
