package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague

import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.SteamGameHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface RocketLeagueComponent : ContentHolderComponent, SteamGameHolderComponent {
    val canLaunch: Boolean
    val eventsToday: StateFlow<EventsState>

    fun back()
    fun launch()

    override fun dismissContent() {
        back()
    }
}