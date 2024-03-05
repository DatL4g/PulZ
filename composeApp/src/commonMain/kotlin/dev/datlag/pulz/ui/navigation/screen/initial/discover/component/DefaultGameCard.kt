package dev.datlag.pulz.ui.navigation.screen.initial.discover.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.pulz.common.bottomShadowBrush
import dev.datlag.pulz.common.mapToIcon
import dev.datlag.pulz.rawg.model.Game
import dev.datlag.pulz.ui.custom.alignment.rememberParallaxAlignment
import dev.datlag.pulz.ui.theme.SchemeTheme
import dev.datlag.pulz.ui.theme.rememberSchemeThemeDominantColorState
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefaultGameCard(
    game: Game,
    isHighlighted: Boolean,
    lazyListState: LazyListState?,
    modifier: Modifier = Modifier,
    onClick: (Game) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.95f,
        animationSpec = tween()
    )

    SchemeTheme(
        key = game.slug
    ) {
        Card(
            modifier = modifier.scale(scale),
            onClick = {
                onClick(game)
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val scope = rememberCoroutineScope()
                val colorState = rememberSchemeThemeDominantColorState(
                    key = game.slug,
                    applyMinContrast = true,
                    minContrastBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )
                val animatedColor by animateColorAsState(
                    targetValue = colorState.color
                )

                AsyncImage(
                    model = game.backgroundImage,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    alignment = if (lazyListState != null) {
                        rememberParallaxAlignment(lazyListState, key = game.id)
                    } else {
                        Alignment.Center
                    },
                    onSuccess = { state ->
                        SchemeTheme.update(
                            key = game.slug,
                            input = state.painter,
                            scope = scope
                        )
                    }
                )

                game.genres.firstOrNull()?.name?.let {
                    GenreChip(
                        label = it,
                        modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
                    )
                }

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .bottomShadowBrush(animatedColor)
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = game.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Rating(game)
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        game.allPlatforms.mapNotNull { it.mapToIcon() }.distinct().forEach { icon ->
                            Image(
                                painter = painterResource(icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(color = LocalContentColor.current)
                            )
                        }
                    }
                }
            }
        }
    }
}