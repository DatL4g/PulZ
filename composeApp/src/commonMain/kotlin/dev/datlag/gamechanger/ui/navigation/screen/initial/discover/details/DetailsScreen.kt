package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastDistinctBy
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.mapToIcon
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.component.ScreenshotCarousel
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsScreen(game: Game, component: DetailsComponent) {
    val dialogState by component.dialog.subscribeAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val padding = PaddingValues(16.dp)
        val isCounterStrike by component.isCounterStrike.collectAsStateWithLifecycle(false)

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

        if (isCounterStrike) {
            FloatingActionButton(
                onClick = component::openCounterStrike,
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