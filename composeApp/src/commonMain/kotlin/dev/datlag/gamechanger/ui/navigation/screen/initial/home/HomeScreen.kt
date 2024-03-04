package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.kmpalette.palette.graphics.Palette
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.other.Constants
import dev.datlag.gamechanger.other.LocalConsentInfo
import dev.datlag.gamechanger.other.Platform
import dev.datlag.gamechanger.ui.custom.BannerAd
import dev.datlag.gamechanger.ui.custom.BrowserClickElevatedCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.component.GameCover
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColor
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColorState
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

@Composable
fun HomeScreen(component: HomeComponent) {
    val childState by component.child.subscribeAsState()
    val consentInfo = LocalConsentInfo.current

    childState.child?.instance?.render() ?: Overview(component)

    LaunchedEffect(consentInfo) {
        consentInfo.initialize()
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
private fun Overview(component: HomeComponent) {
    val padding = PaddingValues(all = 16.dp)

    Box(
        modifier = Modifier.fillMaxSize().padding(LocalPaddingValues.current?.plus(padding) ?: padding),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth().weight(1F), //.haze(state = LocalHaze.current),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                columns = GridCells.Adaptive(512.dp)
            ) {
                item {
                    GameCover(
                        game = Game.Steam.CounterStrike,
                        fallback = SharedRes.images.cs_banner,
                    ) {
                        component.showCounterStrike()
                    }
                }
                item {
                    GameCover(
                        game = Game.Multi.RocketLeague,
                        fallback = SharedRes.images.rl_banner,
                    ) {
                        component.showRocketLeague()
                    }
                }
                item {
                    BrowserClickElevatedCard(
                        uri = Constants.GITHUB,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(SharedRes.images.github),
                                contentDescription = null,
                                modifier = Modifier.size(42.dp),
                                colorFilter = ColorFilter.tint(LocalContentColor.current)
                            )
                            Text(
                                text = stringResource(SharedRes.strings.open_source_info)
                            )
                        }
                    }
                }
            }
            BannerAd(modifier = Modifier.fillMaxWidth())
        }

        if (component.isInstantApp) {
            var requestInstall by remember { mutableStateOf(false) }

            if (requestInstall) {
                Platform.requestInstantAppInstall()
                requestInstall = false
            }

            FloatingActionButton(
                modifier = Modifier.align(Alignment.BottomEnd),
                onClick = {
                    requestInstall = true
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = null
                )
            }
        }
    }
}