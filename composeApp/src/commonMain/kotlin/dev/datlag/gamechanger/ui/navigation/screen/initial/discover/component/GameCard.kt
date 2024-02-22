package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.ui.custom.alignment.rememberParallaxAlignment
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColor
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColorState
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource
import io.github.aakira.napier.Napier

@Composable
fun TrendingGameCard(
    game: Game,
    isHighlighted: Boolean,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.85f,
        animationSpec = tween()
    )
    SchemeTheme(
        key = "${game.id}Trending"
    ) {
        Card(
            modifier = modifier.aspectRatio(1.1F, matchHeightConstraintsFirst = true).scale(scale),
            onClick = {

            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val backgroundImage = remember(game.id) {
                    game.firstPositiveScreenshot?.image ?: game.backgroundImage
                }
                val scope = rememberCoroutineScope()
                var backgroundNotLoaded by remember(game.id) { mutableStateOf<Boolean?>(null) }
                val colorState = rememberSchemeThemeDominantColorState(
                    key = "${game.id}Trending"
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
                            key = "${game.id}Trending",
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
                        containerColor = animatedColor,
                        modifier = Modifier.padding(16.dp).align(Alignment.TopEnd)
                    )
                }

                Row(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.1f to animatedColor.copy(alpha = 0.35f),
                                0.3f to animatedColor.copy(alpha = 0.55f),
                                0.5f to animatedColor.copy(alpha = 0.75f),
                                0.7f to animatedColor.copy(alpha = 0.95f),
                                0.9f to animatedColor
                            )
                        )
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
                                    key = "${game.id}Trending",
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OtherGameCard(
    game: Game,
    isHighlighted: Boolean,
    lazyListState: LazyListState?,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isHighlighted) 1f else 0.95f,
        animationSpec = tween(),
        label = "AnimatedScaleProperty"
    )

    SchemeTheme(
        key = game
    ) {
        Card(
            modifier = modifier.scale(scale),
            onClick = {

            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                val scope = rememberCoroutineScope()
                val colorState = rememberSchemeThemeDominantColorState(
                    key = game,
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
                            key = game,
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
                        .background(
                            brush = Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.1f to animatedColor.copy(alpha = 0.35f),
                                0.3f to animatedColor.copy(alpha = 0.55f),
                                0.5f to animatedColor.copy(alpha = 0.75f),
                                0.7f to animatedColor.copy(alpha = 0.95f),
                                0.9f to animatedColor
                            )
                        )
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
                        game.allPlatforms.mapNotNull { it.name?.mapPlatformToIcon() }.distinct().forEach { icon ->
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

private fun String.mapPlatformToIcon(): ImageResource? {
    return when {
        contains("playstation", ignoreCase = true) -> SharedRes.images.playstation
        contains("pc", ignoreCase = true) -> SharedRes.images.windows
        contains("xbox", ignoreCase = true) -> SharedRes.images.xbox
        contains("Nintendo Switch", ignoreCase = true) -> SharedRes.images.nintendo_switch
        contains("ios", ignoreCase = true) -> SharedRes.images.ios
        contains("android", ignoreCase = true) -> SharedRes.images.android
        contains("linux", ignoreCase = true) -> SharedRes.images.linux
        else -> null
    }
}

@Composable
private fun GenreChip(
    label: String,
    modifier: Modifier = Modifier,
    labelColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    SuggestionChip(
        onClick = {},
        modifier = modifier.wrapContentHeight().height(32.dp),
        border = SuggestionChipDefaults.suggestionChipBorder(enabled = false, borderColor = Color.Transparent, disabledBorderColor = Color.Transparent),
        colors = SuggestionChipDefaults.suggestionChipColors(
            labelColor = labelColor,
            containerColor = containerColor
        ),
        label = {
            Text(
                text = label,
                textAlign = TextAlign.Center
            )
        }
    )
}

@Composable
private fun Rating(game: Game, color: Color = LocalContentColor.current) {
    if (game.rating > 0.0F) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = color
            )
            Text(
                text = game.rating.toString(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = color
            )
        }
    }
}