package dev.datlag.pulz.octane.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.model.Cacheable
import dev.datlag.pulz.model.CatchResult
import dev.datlag.pulz.octane.Octane
import dev.datlag.pulz.octane.model.Game
import dev.datlag.pulz.octane.model.Games
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class GamesMatchStateMachine(
    private val octane: Octane,
    matchId: String
) : FlowReduxStateMachine<GamesMatchStateMachine.State, GamesMatchStateMachine.Action>(
    initialState = State.Loading(matchId)
) {

    private var _currentState: State = State.Loading(matchId)
    val currentState: State
        get() = _currentState

    private val cache = Cacheable<Games>()

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    _currentState = it
                }
                onEnter { state ->
                    cache.getAlive()?.let {
                        return@onEnter state.override { State.Success(it, state.snapshot.matchId) }
                    }

                    val result = CatchResult.result<State> {
                        State.Success(
                            games = octane.games(
                                match = state.snapshot.matchId
                            ).also {
                                cache.cache(it)
                            },
                            matchId = state.snapshot.matchId
                        )
                    }

                    state.override {
                        result.asSuccess {
                            cache.getEvenUnAlive()?.let {
                                State.Success(it, state.snapshot.matchId)
                            } ?: State.Error(state.snapshot.matchId)
                        }
                    }
                }
            }
        }
    }

    sealed interface State {
        data class Loading(internal val matchId: String) : State

        data class Success(val games: List<Game>, internal val matchId: String) : State {
            constructor(games: Games, matchId: String) : this(games.games, matchId)
        }

        data class Error(internal val matchId: String) : State
    }

    sealed interface Action {
        data object Retry : Action
    }
}