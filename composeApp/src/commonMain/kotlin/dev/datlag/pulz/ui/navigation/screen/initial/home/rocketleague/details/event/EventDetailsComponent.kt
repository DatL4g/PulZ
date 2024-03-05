package dev.datlag.pulz.ui.navigation.screen.initial.home.rocketleague.details.event

import dev.datlag.pulz.octane.model.Event
import dev.datlag.pulz.octane.model.Match
import dev.datlag.pulz.octane.state.MatchesEventStateMachine
import dev.datlag.pulz.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface EventDetailsComponent : ContentHolderComponent {

    val event: Event
    val matchesState: StateFlow<MatchesEventStateMachine.State>

    fun back()
    override fun dismissContent() {
        back()
    }

    fun retryLoadingMatches()
    fun showMatch(match: Match)
}