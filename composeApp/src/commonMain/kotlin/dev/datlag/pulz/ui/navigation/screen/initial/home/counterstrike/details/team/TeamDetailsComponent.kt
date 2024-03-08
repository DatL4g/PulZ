package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team

import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.hltv.model.Team
import dev.datlag.pulz.hltv.state.TeamStateMachine
import dev.datlag.pulz.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface TeamDetailsComponent : ContentHolderComponent {

    val initialTeam: Home.Team

    val teamState: StateFlow<TeamStateMachine.State>

    fun back()
    override fun dismissContent() {
        back()
    }

    fun retryLoading()
}