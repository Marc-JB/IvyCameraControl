package com.ivyiot.ipcam_sdk

import com.ivyiot.ipclibrary.model.IvyCamera
import com.ivyiot.ipclibrary.video.IVideoListener

actual interface IvyCameraConnectionNativeState

actual interface IvyCameraConnectionNativeMethods {
    val ivyCamera: IvyCamera

    val videoListener: IVideoListener
}
