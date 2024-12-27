package com.ivyiot.ipcam_sdk

interface IvyCameraConnection : AutoCloseable {
    val uid: String

    val username: String

    suspend fun sendTestCommand()
}
