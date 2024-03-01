package dev.datlag.gamechanger.octane

import dev.datlag.gamechanger.octane.model.Events
import dev.datlag.gamechanger.octane.state.EventsState

internal data object StateSaver {
    var eventsToday: EventsState = EventsState.Loading

    internal data object Cache {
        var eventsToday: Events? = null
    }
}