package nl.marc_apps.ivycameracontrol.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import nl.marc_apps.ivycameracontrol.platform.PlatformNavigateUpIcon

@Composable
fun NavigateUpButton(navController: NavController) {
    IconButton(onClick = { navController.navigateUp() }) {
        Icon(
            imageVector = PlatformNavigateUpIcon,
            contentDescription = null
        )
    }
}
