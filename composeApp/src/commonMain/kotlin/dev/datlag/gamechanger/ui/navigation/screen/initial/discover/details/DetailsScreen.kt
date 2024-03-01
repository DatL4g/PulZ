package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastDistinctBy
import coil3.compose.AsyncImage
import coil3.toUri
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.browserClick
import dev.datlag.gamechanger.common.mapToIcon
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.ui.custom.BrowserClickCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.component.ScreenshotCarousel
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.tooling.Platform
import dev.datlag.tooling.compose.TopStartBottomEndCornerShape
import dev.datlag.tooling.compose.onClick
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier
import io.ktor.http.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsScreen(game: Game, component: DetailsComponent) {
    val dialogState by component.dialog.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val padding = PaddingValues(16.dp)
        val isCounterStrike by component.isCounterStrike.collectAsStateWithLifecycle(false)
        val isRocketLeague by component.isRocketLeague.collectAsStateWithLifecycle(false)

        LazyColumn(
            modifier = Modifier.fillMaxSize().haze(
                state = LocalHaze.current
            ),
            contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Card(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    val scope = rememberCoroutineScope()

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = game.backgroundImage,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth,
                            contentDescription = null,
                            onSuccess = { state ->
                                SchemeTheme.update(
                                    key = game.slug,
                                    input = state.painter,
                                    scope = scope
                                )
                            }
                        )
                        IconButton(
                            onClick = {
                                component.back()
                            },
                            modifier = Modifier.background(Color.Black.copy(alpha = 0.5F), shape = TopStartBottomEndCornerShape(
                                RoundedCornerShape(12.dp)
                            ))
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = stringResource(SharedRes.strings.back),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            item {
                Text(
                    modifier = Modifier.fillParentMaxWidth(),
                    text = game.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            item {
                FlowRow(
                    modifier = Modifier.fillParentMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
                ) {
                    game.platforms.forEach { platform ->
                        platform.platform?.let {
                            Card(
                                modifier = Modifier.fillMaxHeight(),
                                onClick = {
                                    if (platform.requirements != null) {
                                        component.showPlatformRequirements(platform)
                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    it.mapToIcon()?.let { icon ->
                                        Image(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(icon),
                                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                                            contentDescription = it.name
                                        )
                                    }
                                    Text(
                                        text = it.name,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }
            if (game.hasSocials) {
                item {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = stringResource(SharedRes.strings.social),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                item {
                    Row(
                        modifier = Modifier.fillParentMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (!game.website.isNullOrBlank()) {
                                Text(
                                    text = stringResource(SharedRes.strings.website_colon),
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            }
                            if (!game.redditUrl.isNullOrBlank()) {
                                Text(
                                    text = stringResource(SharedRes.strings.reddit_colon),
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            }
                            if (game.metacritic > -1) {
                                Text(
                                    text = stringResource(SharedRes.strings.metacritic_colon),
                                    fontWeight = FontWeight.SemiBold,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1
                                )
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1F),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (!game.website.isNullOrBlank()) {
                                Text(
                                    text = game.websiteTitle ?: game.website!!,
                                    maxLines = 1,
                                    modifier = Modifier.browserClick(game.website)
                                )
                            }
                            if (!game.redditUrl.isNullOrBlank()) {
                                Text(
                                    text = game.redditTitle ?: game.redditUrl!!,
                                    maxLines = 1,
                                    modifier = Modifier.browserClick(game.redditUrl)
                                )
                            }
                            if (game.metacritic > -1) {
                                Text(
                                    text = game.metacritic.toString(),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
            if (game.stores.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = stringResource(SharedRes.strings.stores),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                item {
                    FlowRow(
                        modifier = Modifier.fillParentMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        game.stores.forEach { store ->
                            BrowserClickCard(
                                uri = store.redirect,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    store.mapToIcon()?.let { icon ->
                                        Image(
                                            modifier = Modifier.size(24.dp),
                                            painter = painterResource(icon),
                                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                                            contentDescription = store.title
                                        )
                                    }
                                    Text(text = store.title ?: store.redirect ?: "")
                                }
                            }
                        }
                    }
                }
            }
            game.descriptionRaw?.let { desc ->
                item {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = stringResource(SharedRes.strings.about),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                item(key = desc) {
                    SelectionContainer {
                        Text(text = desc)
                    }
                }
            }
            if (game.screenshots.isNotEmpty()) {
                item {
                    Text(
                        modifier = Modifier.padding(top = 16.dp),
                        text = stringResource(SharedRes.strings.screenshots),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                item(key = game.screenshots) {
                    ScreenshotCarousel(game.screenshots)
                }
            }
        }

        if (isCounterStrike || isRocketLeague) {
            FloatingActionButton(
                onClick = {
                    if (isCounterStrike) {
                        component.openCounterStrike()
                    } else if (isRocketLeague) {
                        component.openRocketLeague()
                    }
                },
                modifier = Modifier.align(Alignment.BottomEnd).padding(LocalPaddingValues.current?.plus(padding) ?: padding)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    }

    dialogState.child?.instance?.render()
}