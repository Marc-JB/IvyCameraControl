package nl.marc_apps.ivycameracontrol.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation.NavController
import com.ivyiot.ipcam_sdk.IvyLivePlayer
import com.ivyiot.ipcam_sdk.LocalCamera
import nl.marc_apps.ivycameracontrol.ui.components.NavigateUpButton
import nl.marc_apps.ivycameracontrol.ui.components.PlatformAlignedTopAppBar
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
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(false)

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

            if (isLoggedIn) {
                IvyLivePlayer(
                    viewModel.ivyCameraConnection!!,
                    modifier = Modifier
                        .aspectRatio(16f / 9f)
                        .fillMaxWidth()
                        .heightIn(max = 400.dp)
                )

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

    DisposableEffect(Unit) {
        onDispose {
            viewModel.logout()
        }
    }
}
