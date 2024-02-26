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

@Composable
fun DefaultOverview(
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

@Composable
private fun Loading() {
    LazyRow(
        modifier = Modifier.fillMaxSize(),
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