package com.ivyiot.ipcam_sdk

import kotlinx.serialization.Serializable

@Serializable
data class LocalCamera(
    val uid: String,
    val macAddress: String,
    val ipAddress: String,
    val port: Long,
    val name: String
)
