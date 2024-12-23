package nl.marc_apps.ivycameracontrol.utils

import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.value
import platform.Foundation.NSError

inline fun <T> createErrorPointer(block: (ObjCObjectVar<NSError?>) -> T): T = memScoped {
    block(alloc<ObjCObjectVar<NSError?>>())
}

fun ObjCObjectVar<NSError?>.throwOnError() {
    if (value != null) {
        throw RuntimeException(value?.debugDescription)
    }
}
