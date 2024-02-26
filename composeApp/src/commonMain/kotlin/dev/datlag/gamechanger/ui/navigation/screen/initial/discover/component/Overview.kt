package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.ui.custom.NativeAdView
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.DiscoverComponent
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun Overview(
    component: DiscoverComponent,
    paddingValues: PaddingValues,
    searchbar: @Composable LazyItemScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
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
                retry = component::retryTopRated
            )
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.esport),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            DefaultOverview(
                state = component.eSportGamesState,
                onClick = component::details,
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
                retry = component::retryCoop
            )
        }
    }
}