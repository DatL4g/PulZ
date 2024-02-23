package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.model.Games
import dev.datlag.gamechanger.rawg.state.ESportGamesStateMachine
import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.rawg.state.TopRatedGamesStateMachine
import dev.datlag.gamechanger.rawg.state.TrendingGamesStateMachine
import dev.datlag.gamechanger.ui.custom.AdView
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.OtherGameCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.TrendingGameCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model.GameSectionType
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun DiscoverScreen(component: DiscoverComponent) {
    val trendingState by component.trendingGamesState.collectAsStateWithLifecycle(TrendingGamesStateMachine.currentState)
    val topRatedState by component.topRatedGamesState.collectAsStateWithLifecycle(TopRatedGamesStateMachine.currentState)
    val eSportState by component.eSportGamesState.collectAsStateWithLifecycle(ESportGamesStateMachine.currentState)

    val padding = PaddingValues(all = 16.dp)
    LazyColumn(
        modifier = Modifier.fillMaxSize().haze(state = LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        item {
            Text(
                text = stringResource(SharedRes.strings.trending),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        TrendingOverview(trendingState)
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.top_rated),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        OtherOverview(topRatedState)
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.esport),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        OtherOverview(eSportState)
        AdView("")
    }
}

private fun LazyListScope.TrendingOverview(
    state: GamesState
) {
    when (state) {
        is GamesState.Loading -> {
            item {
                Text(text = "Loading")
            }
        }
        is GamesState.Error -> {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Retry")
                    }
                }
            }
        }
        is GamesState.Success -> {
            item {
                GameSection(state.games, GameSectionType.Trending)
            }
        }
    }
}

private fun LazyListScope.OtherOverview(
    state: GamesState
) {
    when (state) {
        is GamesState.Loading -> {
            item {
                Text(text = "Loading")
            }
        }
        is GamesState.Error -> {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Retry")
                    }
                }
            }
        }
        is GamesState.Success -> {
            item {
                GameSection(state.games, GameSectionType.Top)
            }
        }
    }
}

@Composable
private fun GameSection(
    games: List<Game>,
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
        itemsIndexed(games, key = { _, it -> it.id }) { index, game ->
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