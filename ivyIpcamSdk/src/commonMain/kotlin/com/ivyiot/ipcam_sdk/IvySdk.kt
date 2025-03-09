package com.ivyiot.ipcam_sdk

import kotlinx.coroutines.flow.Flow

interface IvySdk {
    val version: String

    val localDevices: Flow<List<LocalCamera>>

    suspend fun getLocalDevices(): List<LocalCamera>

    suspend fun login(uid: String, username: String, password: String): IvyCameraConnection
}
