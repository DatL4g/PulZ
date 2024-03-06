package dev.datlag.pulz.octane.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.model.CatchResult
import dev.datlag.pulz.octane.Octane
import dev.datlag.pulz.octane.StateSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class EventsTodayStateMachine(
    private val octane: Octane,
    private val crashlytics: FirebaseFactory.Crashlytics?
) : FlowReduxStateMachine<EventsState, EventsAction>(initialState = currentState) {

    init {
        spec {
            inState<EventsState.Loading> {
                onEnterEffect {
                    currentState = it
                }
                onEnter { state ->
                    StateSaver.Cache.eventsToday.getAlive()?.let {
                        return@onEnter state.override { EventsState.Success(it) }
                    }

                    val result = CatchResult.result<EventsState> {
                        EventsState.Success(
                            octane.events(
                                date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                            ).also {
                                StateSaver.Cache.eventsToday.cache(it)
                            }
                        )
                    }

                    result.onError {
                        crashlytics?.log(it)
                    }

                    state.override {
                        result.asSuccess {
                            StateSaver.Cache.eventsToday.getEvenUnAlive()?.let {
                                EventsState.Success(it)
                            } ?: EventsState.Error
                        }
                    }
                }
            }
            inState<EventsState.Success> {
                onEnterEffect {
                    currentState = it
                }
            }
            inState<EventsState.Error> {
                onEnterEffect {
                    currentState = it
                }
                on<EventsAction.Retry> { _, state ->
                    state.override { EventsState.Loading }
                }
            }
        }
    }

    companion object {
        var currentState: EventsState
            set(value) {
                StateSaver.eventsToday = value
            }
            get() = StateSaver.eventsToday
    }
}