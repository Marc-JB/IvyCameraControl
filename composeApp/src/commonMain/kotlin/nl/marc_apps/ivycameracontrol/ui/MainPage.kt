package nl.marc_apps.ivycameracontrol.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ivyiot.ipcam_sdk.LocalCamera
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import nl.marc_apps.ivycameracontrol.resources.Res
import nl.marc_apps.ivycameracontrol.resources.app_name
import nl.marc_apps.ivycameracontrol.resources.no_cameras_found_on_network
import nl.marc_apps.ivycameracontrol.ui.components.PlatformAlignedTopAppBar
import nl.marc_apps.ivycameracontrol.ui.navigation.CameraDetailRoute
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    navController: NavController,
    viewModel: MainViewModel = koinViewModel()
) {
    val localDevices by viewModel.localDevices.collectAsStateWithLifecycle(emptyList(), minActiveState = Lifecycle.State.RESUMED)

    Scaffold(
        topBar = {
            PlatformAlignedTopAppBar(
                title = { Text(stringResource(Res.string.app_name)) }
            )
        }
    ) { insets ->
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(insets)
                .padding(16.dp)
        ) {
            if (localDevices.isEmpty()) {
                Text(stringResource(Res.string.no_cameras_found_on_network))
            } else {
                LazyColumn {
                    itemsIndexed(localDevices, key = { _, item -> item.uid }) { index, item ->
                        val isLast by remember(index) {
                            derivedStateOf {
                                index == localDevices.lastIndex
                            }
                        }

                        LocalCameraListItem(item, isLast) {
                            navController.navigate(CameraDetailRoute(Json.Default.encodeToString(item)))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LocalCameraListItem(camera: LocalCamera, isLast: Boolean, onClick: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Text(camera.name, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(2.dp))

        Text(camera.uid)

        if (!isLast) {
            Spacer(Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(Modifier.height(2.dp))
        }
    }
}
