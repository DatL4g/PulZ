package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BrushPainter
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.common.shimmer
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model.GameSectionType
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.ui.navigation.screen.initial.component.ErrorContent

@Composable
fun DefaultOverview(
    state: StateFlow<GamesState>,
    onClick: (Game) -> Unit,
    type: GameSectionType.Default,
    retry: () -> Unit
) {
    val loadingState by state.collectAsStateWithLifecycle()

    when (val reachedState = loadingState) {
        is GamesState.Loading -> {
            Loading()
        }
        is GamesState.Error -> {
            val text = when (type) {
                is GameSectionType.Default.TopRated -> SharedRes.strings.games_top_rated_error
                is GameSectionType.Default.ESports -> SharedRes.strings.games_esport_error
                is GameSectionType.Default.OnlineCoop -> SharedRes.strings.games_online_coop_error
                is GameSectionType.Default.Free -> SharedRes.strings.games_free_error
                is GameSectionType.Default.Multiplayer -> SharedRes.strings.games_multiplayer_error
            }
            ErrorContent(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                retry = retry
            )
        }
        is GamesState.Success -> {
            GameSection(reachedState.games, type, onClick)
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
                        .width(200.dp)
                        .height(280.dp)
                        .shimmer(CardDefaults.shape)
                )
            }
        }
    }
}