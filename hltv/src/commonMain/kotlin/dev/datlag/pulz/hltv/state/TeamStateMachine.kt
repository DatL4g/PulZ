package dev.datlag.pulz.hltv.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.hltv.HLTV
import dev.datlag.pulz.hltv.model.Team
import dev.datlag.pulz.model.Cacheable
import dev.datlag.pulz.model.CatchResult
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalCoroutinesApi::class)
class TeamStateMachine(
    private val client: HttpClient,
    private val json: Json,
    private val crashlytics: FirebaseFactory.Crashlytics?,
    href: String
) : FlowReduxStateMachine<TeamStateMachine.State, TeamStateMachine.Action>(
    initialState = State.Loading(href)
) {

    private var _currentState: State = State.Loading(href)
    val currentState: State
        get() = _currentState

    private val cache = Cacheable<Team>()

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    _currentState = it
                }
                onEnter { state ->
                    cache.getAlive()?.let {
                        return@onEnter state.override { State.Success(it, state.snapshot.href) }
                    }

                    val result = CatchResult.result {
                        HLTV.team(client, json, state.snapshot.href)
                    }.mapSuccess<State> {
                        State.Success(
                            team = it,
                            href = state.snapshot.href
                        )
                    }

                    result.onError {
                        crashlytics?.log(it)
                    }

                    state.override {
                        result.asSuccess {
                            cache.getEvenUnAlive()?.let {
                                State.Success(it, state.snapshot.href)
                            } ?: State.Error(state.snapshot.href)
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
                    state.override { State.Loading(state.snapshot.href) }
                }
            }
        }
    }

    sealed interface State {
        data class Loading(internal val href: String) : State
        data class Success(val team: Team, internal val href: String) : State
        data class Error(internal val href: String) : State
    }

    sealed interface Action {
        data object Retry : Action
    }
}