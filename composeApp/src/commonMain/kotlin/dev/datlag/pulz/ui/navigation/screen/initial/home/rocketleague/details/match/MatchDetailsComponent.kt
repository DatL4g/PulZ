package dev.datlag.pulz.ui.navigation.screen.initial.home.rocketleague.details.match

import dev.datlag.pulz.octane.model.Match
import dev.datlag.pulz.octane.state.GamesMatchStateMachine
import dev.datlag.pulz.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface MatchDetailsComponent : ContentHolderComponent {

    val match: Match
    val gamesState: StateFlow<GamesMatchStateMachine.State>

    fun back()
    override fun dismissContent() {
        back()
    }

    fun retryLoadingGames()
}