package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GamesStateMachine
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle

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
            val padding = PaddingValues(all = 16.dp)
            val successState = state as GamesStateMachine.State.Success

            LazyVerticalStaggeredGrid(
                modifier = Modifier.fillMaxSize().haze(state = LocalHaze.current),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalItemSpacing = 8.dp,
                columns = StaggeredGridCells.Adaptive(256.dp),
                contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
            ) {
                items(successState.games.results, key = { it.id }) {
                    GameCard(it)
                }
            }
        }
    }
}

@Composable
private fun GameCard(game: Game) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {

        }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxWidth(),
            model = game.backgroundImage,
            contentDescription = game.name,
            contentScale = ContentScale.FillWidth
        )
    }
}