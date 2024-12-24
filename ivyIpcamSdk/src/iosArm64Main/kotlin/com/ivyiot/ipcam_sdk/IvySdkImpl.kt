package com.ivyiot.ipcam_sdk

import com.ivyiot.ipcam_sdk.errors.AccessDeniedException
import com.ivyiot.ipcam_sdk.errors.DeviceOfflineOrUnreachableException
import com.ivyiot.ipcam_sdk.errors.InvalidCredentialsException
import com.ivyiot.ipcam_sdk.errors.UserLimitReachedException
import com.ivyiot.ipclibrary.sdk.IVYIO_DEV_FOS_NVR
import com.ivyiot.ipclibrary.sdk.IVYIO_DEV_NVR
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_CANCEL_BY_USER
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_DENY
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_MAX_USER
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_OFFLINE
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_OK
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_TIMEOUT
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_USR_OR_PWD_ERR
import com.ivyiot.ipclibrary.sdk.IvyCamera
import com.ivyiot.ipclibrary.sdk.IvyDevLan
import com.ivyiot.ipclibrary.sdk.IvySdkManager
import com.ivyiot.ipclibrary.sdk.loginCamera
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class IvySdkImpl : IvySdk {
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

    override suspend fun login(uid: String, username: String, password: String): IvyCameraConnection {
        val ivyCamera = IvyCamera(uid, username, password)
        suspendCoroutine { continuation ->
            ivyCamera.loginCamera { state, result ->
                if (result == IVYIO_RESULT_OK) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        when (result) {
                            IVYIO_RESULT_USR_OR_PWD_ERR -> InvalidCredentialsException()
                            IVYIO_RESULT_DENY -> AccessDeniedException()
                            IVYIO_RESULT_MAX_USER -> UserLimitReachedException()
                            IVYIO_RESULT_CANCEL_BY_USER -> CancellationException()
                            IVYIO_RESULT_TIMEOUT, IVYIO_RESULT_OFFLINE -> DeviceOfflineOrUnreachableException()
                            else -> RuntimeException()
                        }
                    )
                }
            }
        }
        return IvyCameraConnectionImpl(ivyCamera)
    }

    companion object {
        private const val P2P_REGION_CHINA = 0L
        private const val P2P_REGION_INTERNATIONAL = 1L
    }
}
