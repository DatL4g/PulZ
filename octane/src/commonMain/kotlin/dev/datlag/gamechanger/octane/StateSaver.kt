package dev.datlag.gamechanger.octane

import dev.datlag.gamechanger.model.Cacheable
import dev.datlag.gamechanger.octane.model.Events
import dev.datlag.gamechanger.octane.model.Matches
import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.octane.state.MatchesState

internal data object StateSaver {
    var eventsToday: EventsState = EventsState.Loading
    var matchesToday: MatchesState = MatchesState.Loading
    var eventsUpcoming: EventsState = EventsState.Loading

    internal data object Cache {
        val eventsToday: Cacheable<Events> = Cacheable()
        val matchesToday: Cacheable<Matches> = Cacheable()
        val eventsUpcoming: Cacheable<Events> = Cacheable()
    }
}