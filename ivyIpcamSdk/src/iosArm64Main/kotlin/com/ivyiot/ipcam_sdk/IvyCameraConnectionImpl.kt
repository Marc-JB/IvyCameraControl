package com.ivyiot.ipcam_sdk

import com.ivyiot.ipclibrary.sdk.IvyCamera
import com.ivyiot.ipclibrary.sdk.addEventObserver
import com.ivyiot.ipclibrary.sdk.destroyCamera
import com.ivyiot.ipclibrary.sdk.deviceUID
import com.ivyiot.ipclibrary.sdk.logoutCamera
import com.ivyiot.ipclibrary.sdk.removeEventObserver
import com.ivyiot.ipclibrary.sdk.username
import kotlinx.cinterop.ObjCAction
import platform.darwin.NSObject
import platform.darwin.sel_registerName

class IvyCameraConnectionImpl(private val ivyCamera: IvyCamera) : NSObject(), IvyCameraConnection {
    override val uid = ivyCamera.deviceUID

    override val username = ivyCamera.username

    init {
        ivyCamera.addEventObserver(this, sel_registerName("event"))
    }

    @ObjCAction
    fun event(args: Map<Any?, Any?>) {
        val eventId = args["eventId"] as? UInt
        println("Received message: id = ${eventId}; data = ${args}")
    }

    override fun close() {
        ivyCamera.removeEventObserver(this)
        ivyCamera.logoutCamera()
        ivyCamera.destroyCamera()
    }
}
