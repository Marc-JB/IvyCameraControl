package nl.marc_apps.ivycameracontrol.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object MainPageRoute

@Serializable
data class CameraDetailRoute(val camera: String)
