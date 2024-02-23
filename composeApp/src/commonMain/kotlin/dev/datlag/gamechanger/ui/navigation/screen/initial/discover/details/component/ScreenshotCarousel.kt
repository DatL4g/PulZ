package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.common.calculateCurrentOffsetForPage
import dev.datlag.gamechanger.rawg.model.Game
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun ScreenshotCarousel(screenshot: List<Game.Screenshot>) {
    val pagerState = rememberPagerState(
        initialPage = if (screenshot.size > 2) {
            (screenshot.size.toFloat() / 2F).roundToInt()
        } else {
            0
        }
    ) {
        screenshot.size
    }

    val defaultHeight = 150.dp
    val height = when (calculateWindowSizeClass().widthSizeClass) {
        WindowWidthSizeClass.Compact -> defaultHeight
        WindowWidthSizeClass.Medium -> defaultHeight * 2
        WindowWidthSizeClass.Expanded -> defaultHeight * 2
        else -> defaultHeight
    }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxWidth().height(height),
        pageSpacing = 16.dp
    ) {index ->
        ParallaxCarouselItem(screenshot[index], pagerState, index)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ParallaxCarouselItem(
    screenshot: Game.Screenshot,
    state: PagerState,
    index: Int
) {
    val pageOffset = state.calculateCurrentOffsetForPage(index).absoluteValue

    Card(
        modifier = Modifier.fillMaxSize().graphicsLayer {
            val transformation = lerp(
                start = 0.7F,
                stop = 1F,
                fraction = 1F - pageOffset.coerceIn(0F, 1F)
            )

            scaleY = transformation
            scaleX = transformation
        }
    ) {
        AsyncImage(
            model = screenshot.image,
            modifier = Modifier.fillMaxSize().graphicsLayer {
                val transformation = lerp(
                    start = 1.3F,
                    stop = 1F,
                    fraction = 1F - pageOffset.coerceIn(0F, 1F)
                )

                scaleX = transformation
                scaleY = transformation
            },
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}