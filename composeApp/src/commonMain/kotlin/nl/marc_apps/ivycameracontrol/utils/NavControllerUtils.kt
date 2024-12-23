package nl.marc_apps.ivycameracontrol.utils

import androidx.navigation.NavController

val NavController.canGoBack: Boolean
    get() = previousBackStackEntry != null
