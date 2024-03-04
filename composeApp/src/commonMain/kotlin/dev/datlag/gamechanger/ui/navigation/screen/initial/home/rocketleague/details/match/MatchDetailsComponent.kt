package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.match

import dev.datlag.gamechanger.octane.model.Match
import dev.datlag.gamechanger.octane.state.GamesMatchStateMachine
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
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