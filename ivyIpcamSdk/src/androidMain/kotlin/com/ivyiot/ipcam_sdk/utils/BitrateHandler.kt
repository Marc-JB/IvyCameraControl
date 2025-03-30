package com.ivyiot.ipcam_sdk.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.time.sample
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.seconds

class BitrateHandler {
    private val historicValues = IntArray(30)
    private var nextValueIndex = 0
    private var lastValueInsertionTime = Instant.DISTANT_PAST

    private val mutableBitrate = MutableStateFlow<Int>(0)
    val bitrate = mutableBitrate
        .asStateFlow()
        .sample(1.seconds)

    val currentBitrate: Int
        get() {
            val bitRate = historicValues
                .filterNot { it == 0 }
                .average()

            return if (bitRate.isNaN()) 0 else bitRate.roundToInt()
        }

    fun insertValue(value: Int) {
        lastValueInsertionTime = Clock.System.now()

        historicValues[nextValueIndex] = value

        if (++nextValueIndex > historicValues.lastIndex) {
            nextValueIndex = 0
        }

        mutableBitrate.update { currentBitrate }
    }

    fun resetBitrateWhenUnresponsive() {
        if (Clock.System.now() - lastValueInsertionTime >= 3.seconds) {
            reset()
        }
    }

    private fun reset() {
        historicValues.fill(0)
        mutableBitrate.update { 0 }
    }
}