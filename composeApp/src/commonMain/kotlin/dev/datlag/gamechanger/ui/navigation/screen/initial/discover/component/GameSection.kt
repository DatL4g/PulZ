package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model.GameSectionType
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun GameSection(
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