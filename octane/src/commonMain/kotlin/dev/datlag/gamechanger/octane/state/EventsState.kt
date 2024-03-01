package dev.datlag.gamechanger.octane.state

import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.octane.model.Events
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