package dev.datlag.gamechanger.rawg.state

import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.model.Games

sealed interface GamesState {
    data object Loading : GamesState
    data class Success(
        val games: List<Game>,
    ) : GamesState {
        constructor(games: Games) : this(games.results)
    }
    data class Error(val canRetry: Boolean) : GamesState
}

sealed interface GamesAction {
    data object Retry : GamesAction
}