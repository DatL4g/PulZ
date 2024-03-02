package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague

import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.octane.state.MatchesState
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.MultiGameHolderComponent
import dev.datlag.gamechanger.ui.navigation.SteamGameHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface RocketLeagueComponent : ContentHolderComponent, MultiGameHolderComponent {
    val canLaunch: Boolean
    val eventsToday: StateFlow<EventsState>
    val matchesToday: StateFlow<MatchesState>
    val eventsUpcoming: StateFlow<EventsState>

    fun back()
    fun launch()

    override fun dismissContent() {
        back()
    }

    fun retryLoadingMatchesToday()
    fun retryLoadingEventsToday()
    fun retryLoadingEventsUpcoming()
}