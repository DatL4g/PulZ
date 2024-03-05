package dev.datlag.pulz.rawg.state

import dev.datlag.pulz.rawg.model.Game
import dev.datlag.pulz.rawg.model.Games

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