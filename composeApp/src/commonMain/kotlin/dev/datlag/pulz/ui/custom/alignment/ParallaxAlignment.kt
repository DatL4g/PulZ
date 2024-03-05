package dev.datlag.pulz.ui.custom.alignment

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt

@Composable
fun rememberParallaxAlignment(
    lazyListState: LazyListState,
    key: Any?
): Alignment {
    return remember(lazyListState) {
        ParallaxAlignment(
            horizontalBias = {
                if (key == null) {
                    return@ParallaxAlignment 0F
                }

                // Read the LazyListState layout info
                val layoutInfo = lazyListState.layoutInfo
                // Find the layout info of this item
                val itemInfo = layoutInfo.visibleItemsInfo.firstOrNull { it.key == key } ?: return@ParallaxAlignment 0F

                val adjustedOffset = itemInfo.offset - layoutInfo.viewportStartOffset
                (adjustedOffset / itemInfo.size.toFloat()).coerceIn(-1F, 1F)
            }
        )
    }
}

@Stable
class ParallaxAlignment(
    private val horizontalBias: () -> Float = { 0F },
    private val verticalBias: () -> Float = { 0F }
) : Alignment {
    override fun align(size: IntSize, space: IntSize, layoutDirection: LayoutDirection): IntOffset {
        val centerX = (space.width - size.width).toFloat() / 2F
        val centerY = (space.height - size.height).toFloat() / 2F
        val resolvedHorizontalBias = if (layoutDirection == LayoutDirection.Ltr) {
            horizontalBias()
        } else {
            -1 * horizontalBias()
        }

        val x = centerX * (1 + resolvedHorizontalBias)
        val y = centerY * (1 + verticalBias())
        return IntOffset(x.roundToInt(), y.roundToInt())
    }
}