package com.ivyiot.ipcam_sdk.utils

import kotlin.math.round
import kotlin.math.pow

data class BytesPerSecond(val value: UInt) {
    fun getFormattedValue(): String {
        var speed = value.toDouble()
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

        val decimalCount = if (speed >= 100.0 || unit == "B") 0 else 1
        val decimalShiftValue =  10.0.pow(decimalCount)
        return "${round(speed * decimalShiftValue) / decimalShiftValue} ${unit}/S"
    }
}