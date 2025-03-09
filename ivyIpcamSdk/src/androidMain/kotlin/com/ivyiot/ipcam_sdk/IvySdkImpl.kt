package com.ivyiot.ipcam_sdk

import com.ivyio.sdk.DevType
import com.ivyio.sdk.IvyIoSdkJni
import com.ivyio.sdk.Result
import com.ivyiot.ipcam_sdk.errors.AccessDeniedException
import com.ivyiot.ipcam_sdk.errors.DeviceOfflineOrUnreachableException
import com.ivyiot.ipcam_sdk.errors.InvalidCredentialsException
import com.ivyiot.ipcam_sdk.errors.UserLimitReachedException
import com.ivyiot.ipclibrary.model.EIvyAccountZone
import com.ivyiot.ipclibrary.model.IvyCamera
import com.ivyiot.ipclibrary.sdk.ISdkCallback
import com.ivyiot.ipclibrary.sdk.SDKManager
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration.Companion.seconds

class IvySdkImpl : IvySdk {
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

    override suspend fun login(uid: String, username: String, password: String): IvyCameraConnection {
        val ivyCamera = IvyCamera(uid, username, password)
        val connection = IvyCameraConnectionImpl(ivyCamera)
        connection.login()
        return connection
    }
}
