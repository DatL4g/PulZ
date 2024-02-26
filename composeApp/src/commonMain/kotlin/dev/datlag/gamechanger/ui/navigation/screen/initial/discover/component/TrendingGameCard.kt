package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.common.bottomShadowBrush
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.ui.custom.alignment.rememberParallaxAlignment
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColorState

@Composable
fun TrendingGameCard(
    game: Game,
    isHighlighted: Boolean,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    onClick: (Game) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.85f,
        animationSpec = tween()
    )
    SchemeTheme(
        key = game.slug
    ) {
        Card(
            modifier = modifier.aspectRatio(1.1F, matchHeightConstraintsFirst = true).scale(scale),
            onClick = {
                onClick(game)
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val backgroundImage = remember(game.id) {
                    game.backgroundImage
                }
                val scope = rememberCoroutineScope()
                var backgroundNotLoaded by remember(game.id) { mutableStateOf<Boolean?>(null) }
                val colorState = rememberSchemeThemeDominantColorState(
                    key = game.slug
                )
                val animatedColor by animateColorAsState(
                    targetValue = colorState.color
                )

                AsyncImage(
                    model = backgroundImage,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = game.name,
                    contentScale = ContentScale.Crop,
                    alignment = rememberParallaxAlignment(lazyListState, key = game.id),
                    onSuccess = { state ->
                        backgroundNotLoaded = false
                        SchemeTheme.update(
                            key = game.slug,
                            input = state.painter,
                            scope = scope
                        )
                    },
                    onError = {
                        backgroundNotLoaded = true
                    }
                )

                game.genres.firstOrNull()?.name?.let {
                    GenreChip(
                        label = it,
                        modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .bottomShadowBrush(animatedColor)
                        .padding(16.dp)
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = game.backgroundImage,
                        modifier = Modifier.width(48.dp)
                            .aspectRatio(0.75F)
                            .clip(MaterialTheme.shapes.small),
                        contentDescription = game.name,
                        contentScale = ContentScale.FillBounds,
                        clipToBounds = true,
                        onSuccess = { state ->
                            if (backgroundNotLoaded == true) {
                                SchemeTheme.update(
                                    key = game.slug,
                                    input = state.painter,
                                    scope = scope
                                )
                            }
                        }
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = game.name,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            color = colorState.onColor
                        )
                        Rating(game, colorState.onColor)
                    }
                }
            }
        }
    }
}

