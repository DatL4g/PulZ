package dev.datlag.gamechanger.octane.state

import dev.datlag.gamechanger.octane.model.Match
import dev.datlag.gamechanger.octane.model.Matches

sealed interface MatchesState {
    data object Loading : MatchesState
    data class Success(
        val matches: List<Match>
    ) : MatchesState {
        constructor(matches: Matches) : this(matches.matches)
    }
    data object Error : MatchesState
}

sealed interface MatchesAction {
    data object Retry : MatchesAction
}