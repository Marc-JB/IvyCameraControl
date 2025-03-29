package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.State
import androidx.compose.ui.graphics.ImageBitmap

actual interface IvyCameraConnectionNativeState {
    val liveStreamImages: State<ImageBitmap?>
}

actual interface IvyCameraConnectionNativeMethods {
    fun playLiveStream()

    fun stopLiveStream()
}
