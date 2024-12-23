package com.ivyiot.ipcam_sdk

import com.ivyio.sdk.DevType
import com.ivyio.sdk.IvyIoSdkJni
import com.ivyiot.ipclibrary.model.EIvyAccountZone
import com.ivyiot.ipclibrary.sdk.SDKManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class AndroidIvySdk : IvySdk {
    private val sdkManager by lazy {
        val instance = SDKManager.getInstance()
        IvyIoSdkJni.init()
        instance
    }

    init {
        SDKManager.account_zone = EIvyAccountZone.COM
    }

    override val version: String
        get() = sdkManager.sdkVersion

    override val localDevices = flow {
        while (true) {
            emit(getLocalDevices())
            delay(1.seconds)
        }
    }.distinctUntilChanged()

    override suspend fun getLocalDevices(): List<LocalCamera> {
        return sdkManager.discoveryDeviceInWLAN()
            .filter {
                it.type != DevType.IVY_NVR && it.type != DevType.FOS_NVR
            }.map {
                LocalCamera(it.uid, it.mac, it.ip, it.port.toLong(), it.name)
            }
    }
}
