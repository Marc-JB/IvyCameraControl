package com.ivyiot.ipcam_sdk

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.Flow

actual interface IvyCameraConnectionNativeState {
    val liveStreamImageFlow: Flow<ImageBitmap?>
}

actual interface IvyCameraConnectionNativeMethods {
    fun playLiveStream()

    fun stopLiveStream()
}
