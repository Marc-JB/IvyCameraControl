package com.ivyiot.ipcam_sdk

import com.ivyiot.ipcam_sdk.errors.AccessDeniedException
import com.ivyiot.ipcam_sdk.errors.DeviceOfflineOrUnreachableException
import com.ivyiot.ipcam_sdk.errors.InvalidCredentialsException
import com.ivyiot.ipcam_sdk.errors.UserLimitReachedException
import com.ivyiot.ipcam_sdk.models.EventIds
import com.ivyiot.ipcam_sdk.models.RecordingState
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_CANCEL_BY_USER
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_DENY
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_MAX_USER
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_OFFLINE
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_OK
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_TIMEOUT
import com.ivyiot.ipclibrary.sdk.IVYIO_RESULT_USR_OR_PWD_ERR
import com.ivyiot.ipclibrary.sdk.IVY_CTRL_MSG_ALARM_CHG
import com.ivyiot.ipclibrary.sdk.IVY_CTRL_MSG_GET_WIFI_PARAM
import com.ivyiot.ipclibrary.sdk.IVY_CTRL_MSG_SNAP_PICTURE
import com.ivyiot.ipclibrary.sdk.IvyCamera
import com.ivyiot.ipclibrary.sdk.IvyPlayer
import com.ivyiot.ipclibrary.sdk.IvyPlayerDelegateProtocol
import com.ivyiot.ipclibrary.sdk.IvyVideoDecodeType
import com.ivyiot.ipclibrary.sdk.addEventObserver
import com.ivyiot.ipclibrary.sdk.destroyCamera
import com.ivyiot.ipclibrary.sdk.deviceUID
import com.ivyiot.ipclibrary.sdk.loginCamera
import com.ivyiot.ipclibrary.sdk.logoutCamera
import com.ivyiot.ipclibrary.sdk.removeEventObserver
import com.ivyiot.ipclibrary.sdk.sendCommand
import com.ivyiot.ipclibrary.sdk.username
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSError
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSNumber
import platform.UIKit.UIImage
import platform.darwin.NSObject
import platform.darwin.sel_registerName
import platform.posix.memcpy
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.native.internal.reflect.objCNameOrNull
import kotlin.time.Duration.Companion.seconds

inline fun <T> createErrorPointer(block: (ObjCObjectVar<NSError?>) -> T): T = memScoped {
    block(alloc<ObjCObjectVar<NSError?>>())
}

fun ObjCObjectVar<NSError?>.throwOnError() {
    if (value != null) {
        throw RuntimeException(value?.debugDescription)
    }
}

fun NSDictionary.toJsonString(): String {
    createErrorPointer { errorPointer ->
        val jsonData = NSJSONSerialization.dataWithJSONObject(obj = this, options = 0u, error = errorPointer.ptr)
        try {
            errorPointer.throwOnError()
        } catch (error: RuntimeException) {
            return ""
        }
        return jsonData?.toByteArray()?.decodeToString() ?: ""
    }
}

fun NSData.toByteArray(): ByteArray {
    val byteArray = ByteArray(this.length.toInt())
    byteArray.usePinned {
        memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
    }
    return byteArray
}

class IvyCameraConnectionImpl(private val ivyCamera: IvyCamera) : IvyCameraConnection {
    override val uid = ivyCamera.deviceUID

    override val username = ivyCamera.username

    private val mutableIsLoggedIn = MutableStateFlow(false)
    override val isLoggedIn = mutableIsLoggedIn.asStateFlow()

    private val mutableIsRecording = MutableStateFlow(false)
    override val isRecording = mutableIsRecording.asStateFlow()

    private val mutableLiveStreamImageFlow = MutableStateFlow<UIImage?>(null)
    override val liveStreamImageFlow = mutableLiveStreamImageFlow.asStateFlow()

    private var isLiveStreamActive = false

    private val ivyPlayerDelegate = IvyPlayerDelegateImpl {
        mutableLiveStreamImageFlow.value = it
    }

    private val ivyPlayer = IvyPlayer().also {
        it.delegate = ivyPlayerDelegate
    }

    private val eventHandler = EventHandler {
        val eventId = (it.objectForKey("eventId") as? NSNumber)?.intValue
        val message = it.toJsonString()
        when (eventId) {
            EventIds.RECORDING_STATE_CHANGED -> {
                val recordingState = JsonSerializers.default.decodeFromString<RecordingState>(message)
                mutableIsRecording.update { recordingState.isRecording }
            }
            else -> println("Received message: id = ${eventId}; data = $message")
        }
    }

    init {
        eventHandler.addObserver(ivyCamera)
    }

    override suspend fun login() {
        suspendCoroutine { continuation ->
            ivyCamera.loginCamera { state, result ->
                if (result == IVYIO_RESULT_OK) {
                    mutableIsLoggedIn.update { true }
                    continuation.resume(Unit)
                } else {
                    mutableIsLoggedIn.update { false }
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
    }

    override suspend fun logout() {
        mutableIsLoggedIn.update { false }
        ivyCamera.logoutCamera()
    }

    override suspend fun sendTestCommand() {
        ivyCamera.sendCommand(IVY_CTRL_MSG_GET_WIFI_PARAM.toInt(), emptyMap<Any?, Any?>(), 30.seconds.inWholeMilliseconds.toInt()) { obj, resultCode ->
            if (obj != null) {
                println("Test command result (${obj::class.simpleName}): $obj")
            } else {
                println("Test command result: $obj")
            }
        }
    }

    override fun setFlowSpeed(flowSpeed: Int) {
        TODO("Not yet implemented")
    }

    override fun playLiveStream() {
        if (!isLiveStreamActive) {
            ivyPlayer.playLive(ivyCamera, IvyVideoDecodeType.IvyVideoDecodeUIImage)
            isLiveStreamActive = true
        }
    }

    override fun stopLiveStream() {
        if (isLiveStreamActive) {
            mutableLiveStreamImageFlow.value = null
            isLiveStreamActive = false
            ivyPlayer.stop()
        }
    }

    override fun close() {
        stopLiveStream()
        eventHandler.removeObserver(ivyCamera)
        mutableIsLoggedIn.update { false }
        ivyCamera.logoutCamera()
        ivyCamera.destroyCamera()
    }
}

class IvyPlayerDelegateImpl(val frameReceived: (UIImage) -> Unit) : NSObject(), IvyPlayerDelegateProtocol {
    override fun ivyPlayer(ivyPlayer: IvyPlayer, didReciveFrame: UIImage, isFirstFrame: Boolean) {
        frameReceived(didReciveFrame)
    }
}

class EventHandler(
    val onEvent: (NSDictionary) -> Unit
) : NSObject() {
    @ObjCAction
    fun handleEvent(event: NSDictionary) = onEvent(event)

    fun addObserver(ivyCamera: IvyCamera) {
        ivyCamera.addEventObserver(this, sel_registerName(::handleEvent.name + ":"))
    }

    fun removeObserver(ivyCamera: IvyCamera) {
        ivyCamera.removeEventObserver(this)
    }
}
