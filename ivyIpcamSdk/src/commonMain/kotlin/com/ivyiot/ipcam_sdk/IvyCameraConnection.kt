package com.ivyiot.ipcam_sdk

import kotlinx.coroutines.flow.StateFlow

interface IvyCameraConnectionState {
    val uid: String

    val username: String

    val isLoggedIn: StateFlow<Boolean>

    val isRecording: StateFlow<Boolean>
}

interface IvyCameraConnection : AutoCloseable, IvyCameraConnectionState {
    suspend fun login()

    suspend fun logout()

    suspend fun sendTestCommand()

    fun setFlowSpeed(flowSpeed: Long)
}
