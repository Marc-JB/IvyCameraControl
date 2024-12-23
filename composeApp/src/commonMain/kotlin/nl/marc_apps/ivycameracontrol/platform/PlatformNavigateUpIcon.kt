package nl.marc_apps.ivycameracontrol.platform

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos

val PlatformNavigateUpIcon = when (topAppBarUpIcon) {
    TopAppBarUpIcon.Arrow -> Icons.AutoMirrored.Rounded.ArrowBack
    TopAppBarUpIcon.Chevron -> Icons.AutoMirrored.Rounded.ArrowBackIos
}