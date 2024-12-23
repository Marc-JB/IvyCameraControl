package nl.marc_apps.ivycameracontrol.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nl.marc_apps.ivycameracontrol.platform.PlatformNavigateUpIcon
import nl.marc_apps.ivycameracontrol.platform.platformHandlesSwipeBackGesture
import nl.marc_apps.ivycameracontrol.utils.canGoBack
import kotlin.math.min

private data class DragState(val touchPosition: Offset, val dragOffset: Float) {
    fun updateWith(newTouchPosition: Offset, dragOffsetIncrement: Float): DragState {
        return DragState(newTouchPosition, dragOffset + dragOffsetIncrement)
    }
}

@Composable
fun BackGestureHandler(
    navController: NavController,
    edgeThreshold: Dp = 100.dp,
    distanceThreshold: Dp = 100.dp,
    content: @Composable () -> Unit
) {
    if (platformHandlesSwipeBackGesture) {
        content()
    } else {
        var dragState by remember { mutableStateOf<DragState?>(null) }

        Box(
            Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { offset ->
                            if (navController.canGoBack && offset.x < edgeThreshold.toPx()) {
                                dragState = DragState(offset, 0f)
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            if (dragState != null) {
                                change.consume()
                                dragState = dragState?.updateWith(change.position, dragAmount)
                            }
                        },
                        onDragEnd = {
                            val currentDragState = dragState
                            if (currentDragState != null && currentDragState.dragOffset > distanceThreshold.toPx() && navController.canGoBack) {
                                navController.navigateUp()
                            }
                            dragState = null
                        },
                        onDragCancel = {
                            dragState = null
                        }
                    )
                }
        ) {
            content()

            val currentDragState = dragState
            if (currentDragState != null && currentDragState.touchPosition != Offset.Unspecified) {
                FloatingBackArrow(distanceThreshold, currentDragState)
            }
        }
    }
}

@Composable
private fun FloatingBackArrow(distanceThreshold: Dp, dragState: DragState) {
    val backIconPainter = rememberVectorPainter(PlatformNavigateUpIcon)
    val backIconColor = MaterialTheme.colorScheme.primaryContainer
    val backIconBackgroundColor = MaterialTheme.colorScheme.onPrimaryContainer
    val distanceThresholdPx = with(LocalDensity.current) {
        distanceThreshold.toPx()
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawBackArrow(dragState.touchPosition, distanceThresholdPx, dragState.dragOffset, backIconPainter, backIconColor, backIconBackgroundColor)
    }
}

fun DrawScope.drawBackArrow(touchPosition: Offset, distanceThreshold: Float, dragOffset: Float, vectorPainter: VectorPainter, color: Color, backgroundColor: Color) {
    val iconSize = vectorPainter.intrinsicSize.maxDimension
    val distancePct = min(dragOffset / distanceThreshold, 2f)
    val horizontalDistance = (iconSize * distancePct) - iconSize

    translate(left = horizontalDistance, top = touchPosition.y - iconSize) {
        drawCircle(backgroundColor, iconSize, Offset(iconSize, iconSize))

        translate(left = iconSize / 2f, top = iconSize / 2f) {
            with(vectorPainter) {
                draw(vectorPainter.intrinsicSize, colorFilter = ColorFilter.tint(color))
            }
        }
    }
}
