package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.StateSaver
import dev.datlag.gamechanger.rawg.model.Games
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class GamesStateMachine(
    private val rawg: RAWG,
    private val key: String?
) : FlowReduxStateMachine<GamesStateMachine.State, GamesStateMachine.Action>(initialState = StateSaver.gamesState) {

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    StateSaver.gamesState = it
                }
                onEnter { state ->
                    if (key == null) {
                        return@onEnter state.override { State.Error }
                    }

                    val result = CatchResult.result<State> {
                        State.Success(rawg.games(key))
                    }
                    state.override { result.asSuccess { State.Error } }
                }
            }
            inState<State.Success> {
                onEnterEffect {
                    StateSaver.gamesState = it
                }
            }
            inState<State.Error> {
                onEnterEffect {
                    StateSaver.gamesState = it
                }
                on<Action.Retry> { _, state ->
                    state.override { State.Loading }
                }
            }
        }
    }

    sealed interface State {
        data object Loading : State
        data class Success(val games: Games) : State
        data object Error : State
    }

    sealed interface Action {
        data object Retry : Action
    }

    companion object {
        var currentState: State
            set(value) {
                StateSaver.gamesState = value
            }
            get() = StateSaver.gamesState
    }
}