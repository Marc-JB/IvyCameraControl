package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun IvyLivePlayer(ivyCameraConnection: IvyCameraConnection, modifier: Modifier = Modifier)
