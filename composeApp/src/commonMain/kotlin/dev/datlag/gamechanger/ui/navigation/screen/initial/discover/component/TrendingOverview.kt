package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.common.shimmer
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model.GameSectionType
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TrendingOverview(
    state: StateFlow<GamesState>,
    onClick: (Game) -> Unit,
    retry: () -> Unit
) {
    val loadingState by state.collectAsStateWithLifecycle()

    when (val reachedState = loadingState) {
        is GamesState.Loading -> {
            Loading()
        }
        is GamesState.Error -> {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Loading(false)
                Button(
                    onClick = {
                        retry()
                    }
                ) {
                    Text(text = "Retry")
                }
            }
        }
        is GamesState.Success -> {
            GameSection(reachedState.games, GameSectionType.Trending, onClick)
        }
    }
}

@Composable
private fun Loading(scrollEnabled: Boolean = true) {
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = scrollEnabled
    ) {
        repeat(5) {
            item {
                Box(
                    modifier = Modifier
                        .width(256.dp)
                        .aspectRatio(1.1F, matchHeightConstraintsFirst = true)
                        .shimmer(CardDefaults.shape)
                )
            }
        }
    }
}