package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.pulz.LocalHaze
import dev.datlag.pulz.LocalPaddingValues
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.common.fullRow
import dev.datlag.pulz.common.plus
import dev.datlag.pulz.common.shimmerPainter
import dev.datlag.pulz.game.SteamLauncher
import dev.datlag.pulz.hltv.state.HomeStateMachine
import dev.datlag.pulz.other.StateSaver
import dev.datlag.pulz.ui.custom.RoundTab
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component.DropReset
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component.HLTVContent
import dev.datlag.tooling.compose.TopStartBottomEndCornerShape
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.datlag.tooling.safeSubList
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun CounterStrikeScreen(component: CounterStrikeComponent) {
    val childState by component.child.subscribeAsState()

    childState.child?.instance?.render() ?: MainView(component)
}

@Composable
private fun MainView(component: CounterStrikeComponent) {
    val padding = PaddingValues(16.dp)
    val hltvHome by component.hltvHomeState.collectAsStateWithLifecycle(initialValue = HomeStateMachine.currentState)

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = StateSaver.List.counterStrikeOverview,
        initialFirstVisibleItemScrollOffset = StateSaver.List.counterStrikeOverviewOffset
    )

    val news = remember(hltvHome) {
        (hltvHome as? HomeStateMachine.State.Success)?.home?.news ?: emptyList()
    }
    val newsSize = remember(news) {
        news.size
    }
    var newsShowing by remember { mutableIntStateOf(5) }
    val canShowMoreNews = remember(newsSize, newsShowing) {
        newsSize > newsShowing
    }
    val newsSubList = remember(news, newsShowing) {
        news.safeSubList(0, newsShowing)
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().haze(LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = component.game.heroUrl,
                        contentDescription = stringResource(SharedRes.strings.rocket_league),
                        contentScale = ContentScale.FillWidth,
                        placeholder = shimmerPainter()
                    )
                    IconButton(
                        onClick = {
                            component.back()
                        },
                        modifier = Modifier.background(
                            color = Color.Black.copy(alpha = 0.5F),
                            shape = TopStartBottomEndCornerShape(RoundedCornerShape(12.dp))
                        )
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
            DropReset(component.dropsReset, Modifier.fillParentMaxWidth())
        }
        HLTVContent(
            homeState = hltvHome,
            news = newsSubList,
            canShowMoreNews = canShowMoreNews,
            onShowMoreNews = {
                newsShowing += 5
            },
            retry = {
                component.retryLoadingHLTV()
            },
            articleClick = {
                component.showArticle(it)
            },
            teamClick = {
                component.showTeam(it)
            }
        )
    }

    DisposableEffect(listState) {
        onDispose {
            StateSaver.List.counterStrikeOverview = listState.firstVisibleItemIndex
            StateSaver.List.counterStrikeOverviewOffset = listState.firstVisibleItemScrollOffset
        }
    }
}