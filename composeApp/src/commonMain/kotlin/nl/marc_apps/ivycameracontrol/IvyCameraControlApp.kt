package nl.marc_apps.ivycameracontrol

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.json.Json
import nl.marc_apps.ivycameracontrol.ui.CameraDetailPage
import nl.marc_apps.ivycameracontrol.ui.MainPage
import nl.marc_apps.ivycameracontrol.ui.components.BackGestureHandler
import nl.marc_apps.ivycameracontrol.ui.navigation.CameraDetailRoute
import nl.marc_apps.ivycameracontrol.ui.navigation.MainPageRoute
import org.koin.compose.KoinContext

@Composable
fun IvyCameraControlApp() {
    KoinContext {
        MaterialTheme(
            colorScheme = if(isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()
        ) {
            Surface(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                val navController = rememberNavController()

                BackGestureHandler(navController) {
                    NavHost(
                        navController = navController,
                        startDestination = MainPageRoute,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable<MainPageRoute> {
                            MainPage(navController)
                        }

                        composable<CameraDetailRoute> { backStackEntry ->
                            val route = backStackEntry.toRoute<CameraDetailRoute>()
                            CameraDetailPage(Json.Default.decodeFromString(route.camera), navController)
                        }
                    }
                }
            }
        }
    }
}
