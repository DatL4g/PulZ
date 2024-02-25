package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.model.Games
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class SearchGamesStateMachine(
    private val rawg: RAWG,
    private val key: String?
) : FlowReduxStateMachine<SearchGamesStateMachine.State, SearchGamesStateMachine.Action>(initialState = State.Waiting) {

    init {
        spec {
            inState<State.Waiting> {
                on<Action.Load> { action, state ->
                    state.override { State.Loading(action.query) }
                }
            }
            inState<State.Loading> {
                onEnter { state ->
                    if (key == null) {
                        return@onEnter state.override { State.Error(canRetry = false, query = state.snapshot.query) }
                    }

                    val result = CatchResult.result<State> {
                        State.Success(
                            games = rawg.games(
                                key = key,
                                search = state.snapshot.query
                            )
                        )
                    }

                    state.override { result.asSuccess { State.Error(canRetry = true, query = state.snapshot.query) } }
                }
                on<Action.Clear> { _, state ->
                    state.override { State.Waiting }
                }
            }
            inState<State.Error> {
                on<Action.Retry> { _, state ->
                    if (state.snapshot.canRetry) {
                        state.override { State.Loading(state.snapshot.query) }
                    } else {
                        state.noChange()
                    }
                }
                on<Action.Clear> { _, state ->
                    state.override { State.Waiting }
                }
            }
            inState<State.Success> {
                on<Action.Clear> { _, state ->
                    state.override { State.Waiting }
                }
            }
        }
    }

    sealed interface State {
        data object Waiting : State
        data class Loading(val query: String) : State
        data class Error(val canRetry: Boolean, internal val query: String) : State
        data class Success(val games: List<Game>) : State {
            constructor(games: Games) : this(games.results)
        }
    }

    sealed interface Action {
        data class Load(val query: String) : Action
        data object Retry : Action
        data object Clear : Action
    }
}