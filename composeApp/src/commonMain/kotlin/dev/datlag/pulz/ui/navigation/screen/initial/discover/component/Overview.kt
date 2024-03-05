package dev.datlag.pulz.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.haze
import dev.datlag.pulz.LocalHaze
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.other.StateSaver
import dev.datlag.pulz.ui.custom.NativeAdView
import dev.datlag.pulz.ui.navigation.screen.initial.discover.DiscoverComponent
import dev.datlag.pulz.ui.navigation.screen.initial.discover.model.GameSectionType
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun Overview(
    component: DiscoverComponent,
    paddingValues: PaddingValues,
    searchbar: @Composable LazyItemScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = StateSaver.List.discoverOverview,
        initialFirstVisibleItemScrollOffset = StateSaver.List.discoverOverviewOffset
    )

    LazyColumn(
        state = listState,
        modifier = modifier.haze(state = LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = paddingValues
    ) {
        item {
            searchbar()
        }
        item {
            Text(
                text = stringResource(SharedRes.strings.trending),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            TrendingOverview(
                state = component.trendingGamesState,
                onClick = component::details,
                retry = component::retryTrending
            )
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.top_rated),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            DefaultOverview(
                state = component.topRatedGamesState,
                onClick = component::details,
                type = GameSectionType.Default.TopRated,
                retry = component::retryTopRated
            )
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.esports),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            DefaultOverview(
                state = component.eSportGamesState,
                onClick = component::details,
                type = GameSectionType.Default.ESports,
                retry = component::retryESports
            )
        }
        NativeAdView()
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.online_coop),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            DefaultOverview(
                state = component.coopGamesState,
                onClick = component::details,
                type = GameSectionType.Default.OnlineCoop,
                retry = component::retryCoop
            )
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.free),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            DefaultOverview(
                state = component.freeGamesState,
                onClick = component::details,
                type = GameSectionType.Default.Free,
                retry = component::retryCoop
            )
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.multiplayer),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            DefaultOverview(
                state = component.multiplayerGamesState,
                onClick = component::details,
                type = GameSectionType.Default.Multiplayer,
                retry = component::retryCoop
            )
        }
    }

    DisposableEffect(listState) {
        onDispose {
            StateSaver.List.discoverOverview = listState.firstVisibleItemIndex
            StateSaver.List.discoverOverviewOffset = listState.firstVisibleItemScrollOffset
        }
    }
}