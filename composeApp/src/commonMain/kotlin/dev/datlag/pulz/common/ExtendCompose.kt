package dev.datlag.pulz.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.platform.LocalLayoutDirection
import com.kmpalette.DominantColorState
import dev.datlag.pulz.LocalDarkMode
import dev.datlag.tooling.compose.launchIO
import kotlinx.coroutines.CoroutineScope

fun LazyGridScope.fullRow(
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(span = { GridItemSpan(this.maxLineSpan) }, content = content)
}

fun <T> LazyGridScope.fullRowItems(
    list: List<T>,
    content: @Composable LazyGridItemScope.(T) -> Unit
) {
    items(
        items = list,
        span = {
            GridItemSpan(this.maxLineSpan)
        },
        itemContent = content
    )
}

fun <T> LazyGridScope.fullRowItemsIndexed(
    list: List<T>,
    content: @Composable LazyGridItemScope.(Int, T) -> Unit
) {
    itemsIndexed(
        items = list,
        span = { _, _ ->
            GridItemSpan(this.maxLineSpan)
        },
        itemContent = content
    )
}

@Composable
operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val direction = LocalLayoutDirection.current

    return PaddingValues(
        start = this.calculateStartPadding(direction) + other.calculateStartPadding(direction),
        top = this.calculateTopPadding() + other.calculateTopPadding(),
        end = this.calculateEndPadding(direction) + other.calculateEndPadding(direction),
        bottom = this.calculateBottomPadding() + other.calculateBottomPadding()
    )
}

@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@Composable
private fun shimmerBrush(): Brush {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.4f),
        Color.LightGray.copy(alpha = 0.3f),
        Color.LightGray.copy(alpha = 0.2f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1500.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        )
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(x = translateAnimation - 500, y = 0.0f),
        end = Offset(x = translateAnimation, y = 270F),
    )
}

fun Modifier.shimmer(shape: Shape = RectangleShape): Modifier = composed {
    this.background(
        brush = shimmerBrush(),
        shape = shape
    )
}

@Composable
fun shimmerPainter(): BrushPainter {
    return BrushPainter(shimmerBrush())
}

fun <T : Any> DominantColorState<T>.update(input: T, scope: CoroutineScope) {
    scope.launchIO {
        this@update.updateFrom(input)
    }
}

fun Modifier.bottomShadowBrush(color: Color) = this.background(
    brush = Brush.verticalGradient(
        0.0f to Color.Transparent,
        0.1f to color.copy(alpha = 0.35f),
        0.3f to color.copy(alpha = 0.55f),
        0.5f to color.copy(alpha = 0.75f),
        0.7f to color.copy(alpha = 0.95f),
        0.9f to color
    )
)

@OptIn(ExperimentalComposeUiApi::class)
fun Modifier.autofill(
    autofillTypes: List<AutofillType>,
    onFill: (String) -> Unit,
) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this.onGloballyPositioned {
        autofillNode.boundingBox = it.boundsInWindow()
    }.onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }
}

@Composable
fun ButtonDefaults.googleButtonColors(): ButtonColors {
    return if (LocalDarkMode.current) {
        buttonColors(
            containerColor = Color(0xFFFFFFFF),
            contentColor = Color(0xFF1F1F1F)
        )
    } else {
        buttonColors(
            containerColor = Color(0xFF131314),
            contentColor = Color(0xFFE3E3E3)
        )
    }
}