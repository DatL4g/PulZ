package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.model.Games
import dev.datlag.gamechanger.rawg.state.*
import dev.datlag.gamechanger.ui.custom.AdType
import dev.datlag.gamechanger.ui.custom.AdView
import dev.datlag.gamechanger.ui.custom.NativeAdView
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.OtherGameCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.TrendingGameCard
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model.GameSectionType
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun DiscoverScreen(component: DiscoverComponent) {
    when (calculateWindowSizeClass().widthSizeClass) {
        WindowWidthSizeClass.Expanded -> ExpandedView(component)
        else -> DefaultView(component)
    }
}

@Composable
private fun DefaultView(component: DiscoverComponent) {
    val childState by component.child.subscribeAsState()

    childState.child?.instance?.render() ?: MainView(component, Modifier.fillMaxWidth())
}

@Composable
private fun ExpandedView(component: DiscoverComponent) {
    val childState by component.child.subscribeAsState()

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val modifier = if (childState.child?.configuration != null) {
            Modifier.widthIn(max = 650.dp)
        } else {
            Modifier.fillMaxWidth()
        }
        MainView(component, modifier)

        childState.child?.instance?.let {
            Box(modifier = Modifier.weight(2F)) {
                it.render()
            }
        }
    }
}

@Composable
private fun MainView(component: DiscoverComponent, modifier: Modifier = Modifier) {
    val padding = PaddingValues(all = 16.dp)

    LazyColumn(
        modifier = modifier.haze(state = LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        item {
            Text(
                text = stringResource(SharedRes.strings.trending),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        TrendingOverview(
            component = component,
            onClick = component::details,
            retry = component::retryTrending
        )
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.top_rated),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        OtherOverview(
            state = component.topRatedGamesState,
            initialState = TopRatedGamesStateMachine.currentState,
            onClick = component::details,
            retry = component::retryTopRated
        )
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.esport),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        OtherOverview(
            state = component.eSportGamesState,
            initialState = ESportGamesStateMachine.currentState,
            onClick = component::details,
            retry = component::retryESports
        )
        NativeAdView()
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.online_coop),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        OtherOverview(
            state = component.coopGamesState,
            initialState = OnlineCoopGamesStateMachine.currentState,
            onClick = component::details,
            retry = component::retryCoop
        )
    }
}

private fun LazyListScope.TrendingOverview(
    component: DiscoverComponent,
    onClick: (Game) -> Unit,
    retry: () -> Unit
) {
    item {
        val trendingState by component.trendingGamesState.collectAsStateWithLifecycle(TrendingGamesStateMachine.currentState)

        when (val state = trendingState) {
            is GamesState.Loading -> {
                Text(text = "Loading")
            }
            is GamesState.Error -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = retry
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
            is GamesState.Success -> {
                GameSection(state.games, GameSectionType.Trending, onClick)
            }
        }
    }
}

private fun LazyListScope.OtherOverview(
    state: Flow<GamesState>,
    initialState: GamesState,
    onClick: (Game) -> Unit,
    retry: () -> Unit
) {
    item {
        val loadingState by state.collectAsStateWithLifecycle(initialState)

        when (val reachedState = loadingState) {
            is GamesState.Loading -> {
                Text(text = "Loading")
            }
            is GamesState.Error -> {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = retry
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
            is GamesState.Success -> {
                GameSection(reachedState.games, GameSectionType.Default, onClick)
            }
        }
    }
}

@Composable
private fun GameSection(
    games: List<Game>,
    type: GameSectionType,
    onClick: (Game) -> Unit
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
                        modifier = Modifier.width(256.dp),
                        onClick = onClick
                    )
                }
                else -> {
                    OtherGameCard(
                        game = game,
                        isHighlighted = index == highlightedItem,
                        lazyListState = listState,
                        modifier = Modifier.width(200.dp).height(280.dp),
                        onClick = onClick
                    )
                }
            }
        }
    }
}