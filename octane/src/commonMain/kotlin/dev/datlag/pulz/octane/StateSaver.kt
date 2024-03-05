package dev.datlag.pulz.octane

import dev.datlag.pulz.model.Cacheable
import dev.datlag.pulz.octane.model.Events
import dev.datlag.pulz.octane.model.Matches
import dev.datlag.pulz.octane.state.EventsState
import dev.datlag.pulz.octane.state.MatchesState

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