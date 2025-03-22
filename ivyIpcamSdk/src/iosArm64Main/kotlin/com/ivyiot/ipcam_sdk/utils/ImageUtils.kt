package com.ivyiot.ipcam_sdk.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import org.jetbrains.skia.Image
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation

@OptIn(ExperimentalForeignApi::class)
fun UIImage.toComposeImageBitmap(): ImageBitmap? {
    val bytes = UIImagePNGRepresentation(this) ?: return null

    return Image.makeFromEncoded(bytes.toByteArray())
        .toComposeImageBitmap()
}