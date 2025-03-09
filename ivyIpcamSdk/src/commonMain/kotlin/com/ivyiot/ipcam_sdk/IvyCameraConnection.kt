package com.ivyiot.ipcam_sdk

import com.ivyiot.ipcam_sdk.utils.BytesPerSecond
import kotlinx.coroutines.flow.StateFlow

expect interface IvyCameraConnectionNativeState

interface IvyCameraConnectionState : IvyCameraConnectionNativeState {
    val uid: String

    val username: String

    val isLoggedIn: StateFlow<Boolean>

    val isRecording: StateFlow<Boolean>

    val liveStreamState: StateFlow<LiveStreamState>
}

data class LiveStreamState(
    val isLoading: Boolean = true,
    val flowSpeed: BytesPerSecond? = null
)

expect interface IvyCameraConnectionNativeMethods

interface IvyCameraConnection : AutoCloseable, IvyCameraConnectionState, IvyCameraConnectionNativeMethods {
    suspend fun login()

    suspend fun logout()

    suspend fun sendTestCommand()

    fun setFlowSpeed(flowSpeed: BytesPerSecond?)
}