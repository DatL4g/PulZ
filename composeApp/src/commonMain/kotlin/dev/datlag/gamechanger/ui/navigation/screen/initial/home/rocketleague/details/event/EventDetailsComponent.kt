package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.event

import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.octane.state.MatchesEventStateMachine
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface EventDetailsComponent : ContentHolderComponent {

    val event: Event
    val matchesState: StateFlow<MatchesEventStateMachine.State>

    fun back()
    override fun dismissContent() {
        back()
    }

    fun retryLoadingMatches()
}