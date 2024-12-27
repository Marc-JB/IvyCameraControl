package nl.marc_apps.ivycameracontrol

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
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
                        modifier = Modifier.fillMaxSize(),
                        popExitTransition = {
                            scaleOut(
                                targetScale = 0.9F,
                                transformOrigin = TransformOrigin(pivotFractionX = 0.5f, pivotFractionY = 0.5f)
                            ) + fadeOut(
                                animationSpec = tween(
                                    durationMillis = 200,
                                    easing = CubicBezierEasing(0.1f, 0.1f, 0f, 1f),
                                ),
                            )
                        }
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
