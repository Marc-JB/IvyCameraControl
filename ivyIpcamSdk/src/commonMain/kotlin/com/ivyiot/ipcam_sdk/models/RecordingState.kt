package com.ivyiot.ipcam_sdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecordingState(
    @SerialName("state") val recordingState: Int
) {
    val isRecording
        get() = recordingState == RECORDING

    companion object {
        const val RECORDING = 1
        const val NOT_RECORDING = 0
    }
}