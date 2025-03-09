package com.ivyiot.ipcam_sdk

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset

@Composable
expect fun IvyLivePlayer(ivyCameraConnection: IvyCameraConnection, scale: Float, offset: Offset, modifier: Modifier = Modifier)
