package dev.datlag.pulz.ui.navigation.screen.initial.home.rocketleague

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.datlag.pulz.octane.model.Event
import dev.datlag.pulz.octane.model.Match
import dev.datlag.pulz.octane.state.EventsState
import dev.datlag.pulz.octane.state.MatchesState
import dev.datlag.pulz.ui.navigation.Component
import dev.datlag.pulz.ui.navigation.ContentHolderComponent
import dev.datlag.pulz.ui.navigation.MultiGameHolderComponent
import dev.datlag.pulz.ui.navigation.SteamGameHolderComponent
import kotlinx.coroutines.flow.StateFlow

interface RocketLeagueComponent : ContentHolderComponent, MultiGameHolderComponent {
    val canLaunch: Boolean
    val eventsToday: StateFlow<EventsState>
    val matchesToday: StateFlow<MatchesState>
    val eventsUpcoming: StateFlow<EventsState>

    val child: Value<ChildSlot<RocketLeagueConfig, Component>>

    fun back()
    fun launch()

    override fun dismissContent() {
        back()
    }

    fun retryLoadingMatchesToday()
    fun retryLoadingEventsToday()
    fun retryLoadingEventsUpcoming()

    fun showEventDetails(event: Event)
    fun showMatchDetails(match: Match)
}