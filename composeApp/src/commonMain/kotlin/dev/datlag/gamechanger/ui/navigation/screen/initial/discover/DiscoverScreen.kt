package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.ui.custom.NativeAdView
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.DefaultOverview
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.TrendingOverview
import dev.icerock.moko.resources.compose.stringResource

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
            Search(component)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Search(component: DiscoverComponent) {
    val searchQuery by component.searchQuery.subscribeAsState()

    DockedSearchBar(
        query = searchQuery,
        onQueryChange = {
            component.updateSearchQuery(it)
        },
        onSearch = {
            component.search(it)
        },
        active = false,
        onActiveChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = searchQuery.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                IconButton(
                    onClick = {
                        component.updateSearchQuery("")
                    },
                    enabled = searchQuery.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            }
        },
        placeholder = {
            Text(text = "Search for games")
        },
        modifier = Modifier.fillMaxWidth()
    ) {}
}
