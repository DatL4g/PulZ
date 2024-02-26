package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.common.fullRow
import dev.datlag.gamechanger.rawg.model.Game

@Composable
fun SearchOverview(
    games: List<Game>,
    paddingValues: PaddingValues,
    searchbar: @Composable LazyGridItemScope.() -> Unit,
    onClick: (Game) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.haze(state = LocalHaze.current),
        columns = GridCells.Adaptive(200.dp),
        contentPadding = paddingValues,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fullRow {
            searchbar()
        }
        items(games) { game ->
            DefaultGameCard(
                game = game,
                isHighlighted = false,
                lazyListState = null,
                modifier = Modifier.width(200.dp).height(280.dp),
                onClick = onClick,
            )
        }
    }
}