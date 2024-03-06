package dev.datlag.pulz.hltv.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.hltv.HLTV
import dev.datlag.pulz.hltv.StateSaver
import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.model.CatchResult
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class HomeStateMachine(
    private val client: HttpClient,
    private val crashlytics: FirebaseFactory.Crashlytics?
) : FlowReduxStateMachine<HomeStateMachine.State, HomeStateMachine.Action>(initialState = StateSaver.homeState) {

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    StateSaver.homeState = it
                }
                onEnter { state ->
                    StateSaver.Cache.home?.let {
                        return@onEnter state.override { State.Success(it) }
                    }

                    val result = CatchResult.result<State> {
                        State.Success(
                            home = HLTV.home(client).also {
                                StateSaver.Cache.home = it
                            }
                        )
                    }

                    result.onError {
                        crashlytics?.log(it)
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

    companion object {
        var currentState: State
            set(value) {
                StateSaver.homeState = value
            }
            get() = StateSaver.homeState
    }
}