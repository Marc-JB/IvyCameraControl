package nl.marc_apps.ivycameracontrol.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation.NavController
import com.ivyiot.ipcam_sdk.LocalCamera
import nl.marc_apps.ivycameracontrol.ui.components.NavigateUpButton
import nl.marc_apps.ivycameracontrol.ui.components.PlatformAlignedTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailPage(camera: LocalCamera, navController: NavController) {
    Scaffold(
        topBar = {
            PlatformAlignedTopAppBar(
                title = { Text(camera.name) },
                navigationIcon = { NavigateUpButton(navController) }
            )
        }
    ) { insets ->
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(insets)
                .padding(16.dp)
        ) {
            Text(camera.uid)

            Text(camera.macAddress.chunked(2).fastJoinToString(":"))

            Text("${camera.ipAddress}:${camera.port}")
        }
    }
}