package nl.marc_apps.ivycameracontrol.ui.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

@Composable
fun ZoomableBox(
    modifier: Modifier = Modifier,
    minScale: Float = 0.1f,
    maxScale: Float = 5f,
    content: @Composable ZoomableBoxScope.() -> Unit
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var size by remember { mutableStateOf(IntSize.Zero) }
    Box(
        modifier = modifier
            .clip(RectangleShape)
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale = maxOf(minScale, minOf(scale * zoom, maxScale))
                    val maxX = (size.width * (scale - 1)) / 2
                    val minX = -maxX
                    val maxY = (size.height * (scale - 1)) / 2
                    val minY = -maxY
                    offset = Offset(
                        x = maxOf(minX, minOf(maxX, offset.x + pan.x)),
                        y = maxOf(minY, minOf(maxY, offset.y + pan.y))
                    )
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        if (scale > 1.1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2f
                            offset = calculateOffset(tapOffset, size)
                        }
                    }
                )
            }
    ) {
        val scope = ZoomableBoxScopeImpl(scale, offset)
        scope.content()
    }
}

interface ZoomableBoxScope {
    val scale: Float
    val offset: Offset
}

private data class ZoomableBoxScopeImpl(
    override val scale: Float,
    override val offset: Offset,
) : ZoomableBoxScope

private fun calculateOffset(tapOffset: Offset, size: IntSize): Offset {
    val offsetX = (-(tapOffset.x - (size.width / 2f)) * 2f)
        .coerceIn(-size.width / 2f, size.width / 2f)
    val offsetY = (-(tapOffset.y - (size.height / 2f)) * 2f)
        .coerceIn(-size.height / 2f, size.height / 2f)
    return Offset(offsetX, offsetY)
}