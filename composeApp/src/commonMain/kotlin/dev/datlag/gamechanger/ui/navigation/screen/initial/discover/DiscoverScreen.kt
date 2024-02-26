package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.common.shimmer
import dev.datlag.gamechanger.rawg.state.SearchGamesStateMachine
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.Overview
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component.SearchOverview
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
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
    val searchState by component.searchGamesState.collectAsStateWithLifecycle()
    val padding = PaddingValues(all = 16.dp)
    val localPadding = LocalPaddingValues.current?.plus(padding) ?: padding

    when (val state = searchState) {
        is SearchGamesStateMachine.State.Waiting -> {
            Overview(
                component = component,
                paddingValues = localPadding,
                searchbar = {
                    Search(component)
                },
                modifier = modifier
            )
        }
        is SearchGamesStateMachine.State.Loading -> {
            Loading(localPadding)
        }
        is SearchGamesStateMachine.State.Success -> {
            SearchOverview(
                games = state.games,
                paddingValues = localPadding,
                searchbar = {
                    Search(component)
                },
                onClick = component::details,
                modifier = modifier
            )
        }
        is SearchGamesStateMachine.State.Error -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(localPadding),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {

                    }
                ) {
                    Text(text = "Retry")
                }
            }
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
                contentDescription = stringResource(SharedRes.strings.search)
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
                        contentDescription = stringResource(SharedRes.strings.clear)
                    )
                }
            }
        },
        placeholder = {
            Text(text = stringResource(SharedRes.strings.search_for_games))
        },
        modifier = Modifier.fillMaxWidth()
    ) {}
}

@Composable
private fun Loading(paddingValues: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(5) {
            item {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(280.dp)
                        .shimmer(CardDefaults.shape)
                )
            }
        }
    }
}