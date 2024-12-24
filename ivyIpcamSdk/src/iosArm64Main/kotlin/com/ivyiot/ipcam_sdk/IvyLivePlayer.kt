package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.UIKit.UIImageView

@Composable
actual fun IvyLivePlayer(ivyCameraConnection: IvyCameraConnection, modifier: Modifier) {
    val iosIvyCameraConnection = remember {
        ivyCameraConnection as IvyCameraConnectionImpl
    }
    var shouldOpen by remember { mutableStateOf(true) }
    UIKitView(
        factory = {
            UIImageView()
        },
        modifier = modifier,
        update = {
            if (shouldOpen) {
                shouldOpen = false
                iosIvyCameraConnection.playLiveStream()
                iosIvyCameraConnection.onImageAvailable = { image ->
                    it.image = image
                }
            }
        },
        onRelease = {
            iosIvyCameraConnection.stopLiveStream()
        }
    )
}
