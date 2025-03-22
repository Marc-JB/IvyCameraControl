package com.ivyiot.ipcam_sdk.utils

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.posix.memcpy

fun NSData.toByteArray(): ByteArray {
    val byteArray = ByteArray(length.toInt())

    byteArray.usePinned {
        memcpy(it.addressOf(0), bytes, length)
    }

    return byteArray
}
