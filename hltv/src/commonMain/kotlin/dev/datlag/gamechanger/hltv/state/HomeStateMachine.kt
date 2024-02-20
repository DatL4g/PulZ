package dev.datlag.gamechanger.hltv.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.hltv.HLTV
import dev.datlag.gamechanger.hltv.StateSaver
import dev.datlag.gamechanger.hltv.model.Home
import dev.datlag.gamechanger.model.CatchResult
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class HomeStateMachine(
    private val client: HttpClient
) : FlowReduxStateMachine<HomeStateMachine.State, HomeStateMachine.Action>(initialState = StateSaver.homeState) {

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    StateSaver.homeState = it
                }
                onEnter { state ->
                    val result = CatchResult.result<State> {
                        State.Success(HLTV.home(client))
                    }
                    state.override { result.asSuccess { State.Error } }
                }
            }
            inState<State.Success> {
                onEnterEffect {
                    StateSaver.homeState = it
                }
            }
            inState<State.Error> {
                onEnterEffect {
                    StateSaver.homeState = it
                }
                on<Action.Retry> { _, state ->
                    state.override { State.Loading }
                }
            }
        }
    }

    sealed interface State {
        data object Loading : State
        data class Success(val home: Home) : State
        data object Error : State
    }

    sealed interface Action {
        data object Retry : Action
    }
}