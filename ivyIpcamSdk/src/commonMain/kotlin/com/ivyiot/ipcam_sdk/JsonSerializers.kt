package com.ivyiot.ipcam_sdk

import kotlinx.serialization.json.Json

object JsonSerializers {
    val default = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
}
