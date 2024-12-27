package com.ivyiot.ipcam_sdk

import android.os.Message
import com.ivyio.sdk.IvyIoSdkJni
import com.ivyiot.ipclibrary.model.IvyCamera
import com.ivyiot.ipclibrary.sdk.Cmd
import com.ivyiot.ipclibrary.sdk.CmdHelper
import java.util.Observer

class IvyCameraConnectionImpl(
    val ivyCamera: IvyCamera
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

    override suspend fun sendTestCommand() {
        val resp = CmdHelper.sendCmd(ivyCamera.handle, Cmd.IVY_CTRL_MSG_GET_WIFI_PARAM, null)
        println("Test command result: ${resp.json?.toString(4)}")
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
