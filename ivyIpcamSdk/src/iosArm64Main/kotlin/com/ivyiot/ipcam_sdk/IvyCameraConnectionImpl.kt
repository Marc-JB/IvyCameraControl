package com.ivyiot.ipcam_sdk

import com.ivyiot.ipclibrary.sdk.IVY_CTRL_MSG_GET_WIFI_PARAM
import com.ivyiot.ipclibrary.sdk.IVY_CTRL_MSG_SNAP_PICTURE
import com.ivyiot.ipclibrary.sdk.IvyCamera
import com.ivyiot.ipclibrary.sdk.IvyPlayer
import com.ivyiot.ipclibrary.sdk.IvyPlayerDelegateProtocol
import com.ivyiot.ipclibrary.sdk.IvyVideoDecodeType
import com.ivyiot.ipclibrary.sdk.addEventObserver
import com.ivyiot.ipclibrary.sdk.destroyCamera
import com.ivyiot.ipclibrary.sdk.deviceUID
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
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSError
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSNumber
import platform.UIKit.UIImage
import platform.darwin.NSObject
import platform.darwin.sel_registerName
import platform.posix.memcpy
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

    private val mutableLiveStreamImageFlow = MutableStateFlow<UIImage?>(null)
    val liveStreamImageFlow = mutableLiveStreamImageFlow.asStateFlow()

    private var isLiveStreamActive = false

    var onImageAvailable: ((UIImage) -> Unit)? = null

    private val ivyPlayerDelegate = IvyPlayerDelegateImpl {
        mutableLiveStreamImageFlow.update { it }
        onImageAvailable?.invoke(it)
    }

    private val ivyPlayer = IvyPlayer().also {
        it.delegate = ivyPlayerDelegate
    }

    private val eventHandler = EventHandler {
        val eventId = it.objectForKey("eventId") as? NSNumber
        println("Received message: id = ${eventId?.unsignedIntValue}; data = ${it.toJsonString()}")
    }

    init {
        eventHandler.addObserver(ivyCamera)
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

    fun playLiveStream() {
        if (!isLiveStreamActive) {
            ivyPlayer.playLive(ivyCamera, IvyVideoDecodeType.IvyVideoDecodeUIImage)
            isLiveStreamActive = true
        }
    }

    fun stopLiveStream() {
        if (isLiveStreamActive) {
            mutableLiveStreamImageFlow.update { null }
            isLiveStreamActive = false
            ivyPlayer.stop()
        }
    }

    override fun close() {
        stopLiveStream()
        eventHandler.removeObserver(ivyCamera)
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
