package com.ivyiot.ipcam_sdk

import com.ivyiot.ipclibrary.sdk.IvyCamera
import com.ivyiot.ipclibrary.sdk.addEventObserver
import com.ivyiot.ipclibrary.sdk.destroyCamera
import com.ivyiot.ipclibrary.sdk.deviceUID
import com.ivyiot.ipclibrary.sdk.logoutCamera
import com.ivyiot.ipclibrary.sdk.removeEventObserver
import com.ivyiot.ipclibrary.sdk.username
import kotlinx.cinterop.ObjCAction
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import kotlinx.serialization.json.internal.decodeToSequenceByReader
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.NSError
import platform.Foundation.NSJSONSerialization
import platform.Foundation.NSMutableDictionary
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.allKeys
import platform.Foundation.dictionaryWithDictionary
import platform.Foundation.initWithData
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.darwin.sel_registerName
import platform.posix.memcpy

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

    private val eventHandler = EventHandler {
        val eventId = it.objectForKey("eventId") as? NSNumber
        println("Received message: id = ${eventId?.unsignedIntValue}; data = ${it.toJsonString()}")
    }

    init {
        eventHandler.addObserver(ivyCamera)
    }

    override fun close() {
        eventHandler.removeObserver(ivyCamera)
        ivyCamera.logoutCamera()
        ivyCamera.destroyCamera()
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
