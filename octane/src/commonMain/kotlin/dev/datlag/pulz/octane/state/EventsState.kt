package dev.datlag.pulz.octane.state

import dev.datlag.pulz.octane.model.Event
import dev.datlag.pulz.octane.model.Events
import kotlinx.collections.immutable.ImmutableList

sealed interface EventsState {
    data object Loading : EventsState
    data class Success(
        val events: List<Event>
    ) : EventsState {
        constructor(events: Events) : this(events.events)
    }
    data object Error : EventsState
}

sealed interface EventsAction {
    data object Retry : EventsAction
}