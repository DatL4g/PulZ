package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.model.Games
import dev.datlag.gamechanger.rawg.state.GamesStateMachine
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.OtherGameCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.TrendingGameCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model.GameSectionType
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun DiscoverScreen(component: DiscoverComponent) {
    val state by component.gamesState.collectAsStateWithLifecycle(GamesStateMachine.currentState)

    when (state) {
        is GamesStateMachine.State.Loading -> {
            Text(text = "Loading games")
        }
        is GamesStateMachine.State.Error -> {
            Text(text = "Error loading games")
        }
        is GamesStateMachine.State.Success -> {
            val successState = state as GamesStateMachine.State.Success
            GameOverview(
                successState.trendingGames,
                successState.topGames
            )
        }
    }
}

@Composable
private fun GameOverview(
    trendingGames: Games?,
    topGames: Games?,
) {
    val padding = PaddingValues(all = 16.dp)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        if (trendingGames != null) {
            item {
                Text(
                    text = stringResource(SharedRes.strings.trending),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            item {
                GameSection(trendingGames, GameSectionType.Trending)
            }
        }
        if (topGames != null) {
            item {
                Text(
                    text = stringResource(SharedRes.strings.top_rated),
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            item {
                GameSection(topGames, GameSectionType.Top)
            }
        }
    }
}

@Composable
private fun GameSection(
    games: Games,
    type: GameSectionType
) {
    val listState = rememberLazyListState()
    var highlightedItem by remember { mutableIntStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            layoutInfo.visibleItemsInfo
                .firstOrNull { it.offset >= layoutInfo.viewportStartOffset }
                ?.index ?: 0
        }.distinctUntilChanged()
            .collect {
                highlightedItem = it
            }
    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(games.results, key = { _, it -> it.id }) { index, game ->
            when (type) {
                GameSectionType.Trending -> {
                    TrendingGameCard(
                        game = game,
                        isHighlighted = index == highlightedItem,
                        lazyListState = listState,
                        modifier = Modifier
                            .width(256.dp)
                    )
                }
                else -> {
                    OtherGameCard(
                        game = game,
                        isHighlighted = index == highlightedItem,
                        lazyListState = listState,
                        modifier = Modifier.width(200.dp).height(280.dp)
                    )
                }
            }
        }
    }
}