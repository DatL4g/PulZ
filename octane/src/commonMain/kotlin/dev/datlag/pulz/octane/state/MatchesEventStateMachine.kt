package dev.datlag.pulz.octane.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.model.Cacheable
import dev.datlag.pulz.model.CatchResult
import dev.datlag.pulz.octane.Octane
import dev.datlag.pulz.octane.model.Match
import dev.datlag.pulz.octane.model.Matches
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MatchesEventStateMachine(
    private val octane: Octane,
    private val crashlytics: FirebaseFactory.Crashlytics?,
    eventId: String
) : FlowReduxStateMachine<MatchesEventStateMachine.State, MatchesEventStateMachine.Action>(
    initialState = State.Loading(eventId)
) {

    private var _currentState: State = State.Loading(eventId)
    val currentState: State
        get() = _currentState

    private val cache = Cacheable<Matches>()

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    _currentState = it
                }
                onEnter { state ->
                    cache.getAlive()?.let {
                        return@onEnter state.override { State.Success(it, state.snapshot.eventId) }
                    }

                    val result = CatchResult.result {
                        octane.matches(
                            event = state.snapshot.eventId
                        )
                    }.validateSuccess { it.matches.isNotEmpty() }.resultOnError {
                        octane.eventMatches(state.snapshot.eventId)
                    }.mapSuccess<State> {
                        State.Success(
                            matches = it,
                            eventId = state.snapshot.eventId
                        )
                    }

                    result.onError {
                        crashlytics?.log(it)
                    }

                    state.override {
                        result.asSuccess {
                            cache.getEvenUnAlive()?.let {
                                State.Success(it, state.snapshot.eventId)
                            } ?: State.Error(state.snapshot.eventId)
                        }
                    }
                }
            }
            inState<State.Success> {
                onEnterEffect {
                    _currentState = it
                }
            }
            inState<State.Error> {
                onEnterEffect {
                    _currentState = it
                }
                on<Action.Retry> { _, state ->
                    state.override { State.Loading(state.snapshot.eventId) }
                }
            }
        }
    }

    sealed interface State {
        data class Loading(internal val eventId: String) : State

        data class Success(val matches: List<Match>, internal val eventId: String) : State {
            constructor(matches: Matches, eventId: String) : this(matches.matches, eventId)
        }

        data class Error(internal val eventId: String) : State
    }

    sealed interface Action {
        data object Retry : Action
    }
}