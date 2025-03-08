package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.cinterop.useContents
import platform.CoreGraphics.CGAffineTransform
import platform.CoreGraphics.CGAffineTransformIdentity
import platform.CoreGraphics.CGAffineTransformMakeScale
import platform.CoreGraphics.CGAffineTransformMakeTranslation
import platform.CoreGraphics.CGAffineTransformScale
import platform.CoreGraphics.CGAffineTransformTranslate
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun IvyLivePlayer(
    ivyCameraConnection: IvyCameraConnection,
    scale: Float,
    offset: Offset,
    modifier: Modifier
) {
    val iosIvyCameraConnection = ivyCameraConnection as IvyCameraConnectionImpl
    var isInitialised by remember { mutableStateOf(false) }
    val image by ivyCameraConnection.liveStreamImageFlow.collectAsStateWithLifecycle(null)
    val transformation = remember(scale, offset) {
        CGAffineTransformScale(CGAffineTransformMakeTranslation(offset.x / 2.0, offset.y / 2.0), scale.toDouble(), scale.toDouble())
    }

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
            it.transform = transformation
        },
        onRelease = {
            iosIvyCameraConnection.stopLiveStream()
        },
        properties = UIKitInteropProperties(interactionMode = null)
    )
}
