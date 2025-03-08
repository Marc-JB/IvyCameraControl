package nl.marc_apps.ivycameracontrol.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ivyiot.ipcam_sdk.IvyLivePlayer
import com.ivyiot.ipcam_sdk.LocalCamera
import nl.marc_apps.ivycameracontrol.ui.components.NavigateUpButton
import nl.marc_apps.ivycameracontrol.ui.components.PlatformAlignedTopAppBar
import nl.marc_apps.ivycameracontrol.ui.components.RecordingIndicator
import nl.marc_apps.ivycameracontrol.ui.components.ZoomableBox
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraDetailPage(
    camera: LocalCamera,
    navController: NavController,
    viewModel: CameraDetailViewModel = koinViewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle(false)
    val isRecording by viewModel.isRecording.collectAsStateWithLifecycle(false)
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            PlatformAlignedTopAppBar(
                title = { Text(camera.name) },
                navigationIcon = { NavigateUpButton(navController) }
            )
        }
    ) { insets ->
        Column (
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(insets)
                .fillMaxSize()
        ) {
            if (isLoggedIn) {
                Surface(tonalElevation = 1.dp) {
                    Column {
                        ZoomableBox(
                            minScale = 1f,
                            maxScale = 3f,
                            modifier = Modifier
                                .aspectRatio(16f / 9f)
                                .fillMaxWidth()
                                .heightIn(max = 400.dp)
                        ) {
                            IvyLivePlayer(
                                viewModel.ivyCameraConnection!!,
                                scale,
                                offset,
                                Modifier.fillMaxSize()
                            )
                        }

                        Row (
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            RecordingIndicator(isRecording)
                        }
                    }
                }
            }

            Column (
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
            ) {
                CameraDebugInfo(camera)

                if (isLoggedIn) {
                    Button(
                        onClick = {
                            viewModel.sendTestCommand()
                        }
                    ) {
                        Text("Send test command")
                    }
                } else {
                    TextField(username,
                        onValueChange = {
                            username = it
                        },
                        label = { Text("Username") },
                        singleLine = true)

                    TextField(password,
                        onValueChange = {
                            password = it
                        },
                        label = { Text("Password") },
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val icon = if (showPassword) {
                                Icons.Rounded.Visibility
                            } else {
                                Icons.Rounded.VisibilityOff
                            }

                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    icon,
                                    contentDescription = "Visibility"
                                )
                            }
                        })

                    Button(
                        onClick = {
                            viewModel.login(camera.uid, username, password)
                        }
                    ) {
                        Text("login")
                    }
                }
            }
        }
    }
}

@Composable
private fun CameraDebugInfo(camera: LocalCamera) {
    SelectionContainer {
        Column (
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(camera.uid)

            Text(camera.macAddress.chunked(2).fastJoinToString(":"))

            Text("${camera.ipAddress}:${camera.port}")
        }
    }
}
