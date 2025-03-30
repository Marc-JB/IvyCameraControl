package com.ivyiot.ipcam_sdk.utils

import android.os.Build

object HardwareDecodeUtils {
    private val exclusions = arrayOf("HUAWEI P6-T00", "H60-L01", "Coolpad 8675", "smartisan", "Redmi 6 Pro")

    @get:JvmStatic
    val deviceDoesNotSupportHardwareDecode by lazy {
        for (exclusion in exclusions) {
            if (Build.MODEL.endsWith(exclusion, ignoreCase = true)) {
                return@lazy true
            }

            if (Build.MANUFACTURER.equals(exclusion, ignoreCase = true)) {
                return@lazy true
            }
        }

        false
    }
}
