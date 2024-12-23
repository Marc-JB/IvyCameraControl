package com.ivyiot.ipcam_sdk

import kotlinx.coroutines.flow.Flow

interface IvySdk {
    val version: String

    val localDevices: Flow<List<LocalCamera>>

    suspend fun getLocalDevices(): List<LocalCamera>
}
