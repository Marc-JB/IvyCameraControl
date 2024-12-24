package com.ivyiot.ipcam_sdk

import android.os.Message
import com.ivyiot.ipclibrary.model.IvyCamera
import java.util.Observer

class IvyCameraConnectionImpl(
    private val ivyCamera: IvyCamera
) : IvyCameraConnection {
    override val uid: String = ivyCamera.uid

    override val username: String = ivyCamera.usrName

    private val observer = Observer { _, argument ->
        if (argument is Message) {
            onEventReceived(argument)
        }
    }

    init {
        ivyCamera.addObserver(observer)
    }

    private fun onEventReceived(message: Message) {
        println("Received message: id = ${message.what}; data = ${message.obj}")
    }

    override fun close() {
        ivyCamera.deleteObserver(observer)
        ivyCamera.logout()
        ivyCamera.destroy()
    }
}
