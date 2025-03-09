package com.ivyiot.ipcam_sdk

import kotlinx.coroutines.flow.Flow
import platform.UIKit.UIImage

actual interface IvyCameraConnectionNativeState {
    val liveStreamImageFlow: Flow<UIImage?>
}

actual interface IvyCameraConnectionNativeMethods {
    fun playLiveStream()

    fun stopLiveStream()
}
