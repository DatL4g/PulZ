package dev.datlag.pulz.octane.state

import dev.datlag.pulz.octane.model.Match
import dev.datlag.pulz.octane.model.Matches

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