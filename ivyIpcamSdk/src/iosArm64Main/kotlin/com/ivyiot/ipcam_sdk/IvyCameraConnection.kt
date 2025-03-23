package com.ivyiot.ipcam_sdk

import kotlinx.coroutines.flow.Flow
import org.jetbrains.skia.Image

actual interface IvyCameraConnectionNativeState {
    val liveStreamImageFlow: Flow<Image?>
}

actual interface IvyCameraConnectionNativeMethods {
    fun playLiveStream()

    fun stopLiveStream()
}
