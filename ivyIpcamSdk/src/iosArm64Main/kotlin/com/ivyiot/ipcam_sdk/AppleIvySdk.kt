package com.ivyiot.ipcam_sdk

import com.ivyiot.ipclibrary.sdk.IVYIO_DEV_FOS_NVR
import com.ivyiot.ipclibrary.sdk.IVYIO_DEV_NVR
import com.ivyiot.ipclibrary.sdk.IvyDevLan
import com.ivyiot.ipclibrary.sdk.IvySdkManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

class AppleIvySdk : IvySdk {
    private val sdkManager by lazy {
        val instance = IvySdkManager.shared()
        instance.setP2PRegion(P2P_REGION_INTERNATIONAL)
        instance.initializer()
        instance
    }

    override val version: String
        get() = sdkManager.versionInfo()

    override val localDevices = flow {
        while (true) {
            emit(getLocalDevices())
            delay(1.seconds)
        }
    }.distinctUntilChanged()

    override suspend fun getLocalDevices(): List<LocalCamera> {
        val devices = suspendCoroutine { continuation ->
            sdkManager.searchDevices { devices ->
                continuation.resume(devices)
            }
        }

        return devices?.mapNotNull {
            it as? IvyDevLan
        }?.filter {
            val type = it.type.toUInt()
            type != IVYIO_DEV_NVR && type != IVYIO_DEV_FOS_NVR
        }?.map {
            LocalCamera(it.uid, it.mac, it.ip, it.port, it.name)
        } ?: emptyList()
    }

    companion object {
        private const val P2P_REGION_CHINA = 0L
        private const val P2P_REGION_INTERNATIONAL = 1L
    }
}
