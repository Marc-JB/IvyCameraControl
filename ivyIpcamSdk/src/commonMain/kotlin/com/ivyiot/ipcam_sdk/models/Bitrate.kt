package com.ivyiot.ipcam_sdk.models

import kotlin.math.pow
import kotlin.math.round

data class Bitrate(val bytesPerSecond: UInt) {
    fun getFormattedValue(): String {
        var speed = bytesPerSecond.toDouble()
        var unit = "B"

        if (speed >= 1024) {
            speed /= 1024
            unit = "kB"
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
        return "${round(speed * decimalShiftValue) / decimalShiftValue} ${unit}/s"
    }
}