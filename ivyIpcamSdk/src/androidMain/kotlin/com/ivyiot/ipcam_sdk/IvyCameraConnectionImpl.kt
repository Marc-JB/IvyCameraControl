package com.ivyiot.ipcam_sdk

import android.os.Message
import com.ivyio.sdk.Result
import com.ivyiot.ipcam_sdk.errors.AccessDeniedException
import com.ivyiot.ipcam_sdk.errors.DeviceOfflineOrUnreachableException
import com.ivyiot.ipcam_sdk.errors.InvalidCredentialsException
import com.ivyiot.ipcam_sdk.errors.UserLimitReachedException
import com.ivyiot.ipcam_sdk.models.EventIds
import com.ivyiot.ipcam_sdk.models.RecordingState
import com.ivyiot.ipclibrary.model.IvyCamera
import com.ivyiot.ipclibrary.sdk.Cmd
import com.ivyiot.ipclibrary.sdk.CmdHelper
import com.ivyiot.ipclibrary.sdk.ISdkCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale
import java.util.Observer
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IvyCameraConnectionImpl(
    val ivyCamera: IvyCamera
) : IvyCameraConnection {
    override val uid: String = ivyCamera.uid

    override val username: String = ivyCamera.usrName

    private val mutableIsLoggedIn = MutableStateFlow(false)
    override val isLoggedIn = mutableIsLoggedIn.asStateFlow()

    private val mutableIsRecording = MutableStateFlow(false)
    override val isRecording = mutableIsRecording.asStateFlow()

    private var flowSpeedUpdateCounter = 0

    private val observer = Observer { _, argument ->
        if (argument is Message) {
            onEventReceived(argument)
        }
    }

    init {
        ivyCamera.addObserver(observer)
    }

    override suspend fun login() {
        suspendCoroutine { continuation ->
            ivyCamera.loginDevice(object : ISdkCallback<Nothing?> {
                override fun onSuccess(nothing: Nothing?) {
                    mutableIsLoggedIn.update { true }
                    continuation.resume(Unit)
                }

                override fun onError(errorCode: Int) = handleError(errorCode)

                override fun onLoginError(errorCode: Int) = handleError(errorCode)

                private fun handleError(errorCode: Int) {
                    mutableIsLoggedIn.update { false }
                    continuation.resumeWithException(
                        when (errorCode) {
                            Result.USR_OR_PWD_ERR -> InvalidCredentialsException()
                            Result.DENY -> AccessDeniedException()
                            Result.MAX_USER -> UserLimitReachedException()
                            Result.CANCEL_BY_USER -> CancellationException()
                            Result.TIMEOUT, Result.OFFLINE -> DeviceOfflineOrUnreachableException()
                            else -> RuntimeException()
                        }
                    )
                }
            })
        }
    }

    override suspend fun logout() {
        mutableIsLoggedIn.update { false }
        ivyCamera.logout()
    }

    override suspend fun sendTestCommand() {
        val resp = CmdHelper.sendCmd(ivyCamera.handle, Cmd.IVY_CTRL_MSG_GET_WIFI_PARAM, null)
        println("Test command result: ${resp.json?.toString(4)}")
    }

    override fun setFlowSpeed(flowSpeed: Int) {
        var speed = flowSpeed.toFloat()
        var unit = "B"

        if (speed >= 1024) {
            speed /= 1024
            unit = "KB"
        }

        if (speed >= 1024) {
            speed /= 1024
            unit = "MB"
        }

        if (speed >= 1024) {
            speed /= 1024
            unit = "GB"
        }

        val formattedSpeed = String.format(if (speed >= 100.0f || unit == "B") "%.0f" else "%.1f", speed) + " ${unit}/S"

        flowSpeedUpdateCounter++

        if (flowSpeedUpdateCounter >= 10) {
            println("Net flow speed: $formattedSpeed")
            flowSpeedUpdateCounter = 0
        }
    }

    private fun onEventReceived(message: Message) {
        when (message.what) {
            EventIds.RECORDING_STATE_CHANGED -> {
                val recordingState = JsonSerializers.default.decodeFromString<RecordingState>(message.obj.toString())
                mutableIsRecording.update { recordingState.isRecording }
            }
            else -> println("Received message: id = ${message.what}; data = ${message.obj}")
        }
    }

    override fun close() {
        ivyCamera.deleteObserver(observer)
        mutableIsLoggedIn.update { false }
        ivyCamera.logout()
        ivyCamera.destroy()
    }
}
