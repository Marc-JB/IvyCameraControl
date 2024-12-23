package nl.marc_apps.ivycameracontrol.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import nl.marc_apps.ivycameracontrol.platform.TopAppBarAlignment
import nl.marc_apps.ivycameracontrol.platform.topAppBarAlignment

@ExperimentalMaterial3Api
@Composable
fun PlatformAlignedTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpandedHeight,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    startAlignedTopAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    centerAlignedTopAppBarColors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    when (topAppBarAlignment) {
        TopAppBarAlignment.Start -> TopAppBar(title, modifier, navigationIcon, actions, expandedHeight, windowInsets, startAlignedTopAppBarColors, scrollBehavior)
        TopAppBarAlignment.Center -> CenterAlignedTopAppBar(title, modifier, navigationIcon, actions, expandedHeight, windowInsets, centerAlignedTopAppBarColors, scrollBehavior)
    }
}
