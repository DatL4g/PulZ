package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.StateSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class FreeGamesStateMachine(
    private val rawg: RAWG,
    private val key: String?
) : FlowReduxStateMachine<GamesState, GamesAction>(initialState = currentState) {
    init {
        spec {
            inState<GamesState.Loading> {
                onEnterEffect {
                    currentState = it
                }
                onEnter { state ->
                    StateSaver.Cache.free?.let {
                        return@onEnter state.override { GamesState.Success(it) }
                    }
                    if (key == null) {
                        return@onEnter state.override { GamesState.Error(canRetry = false) }
                    }

                    val result = CatchResult.result<GamesState> {
                        GamesState.Success(
                            rawg.games(
                                key = key,
                                tags = "79"
                            ).also {
                                StateSaver.Cache.free = it
                            }
                        )
                    }

                    state.override { result.asSuccess { GamesState.Error(canRetry = true) } }
                }
            }
            inState<GamesState.Success> {
                onEnterEffect {
                    currentState = it
                }
            }
            inState<GamesState.Error> {
                onEnterEffect {
                    currentState = it
                }
                on<GamesAction.Retry> { _, state ->
                    if (state.snapshot.canRetry) {
                        state.override { GamesState.Loading }
                    } else {
                        state.noChange()
                    }
                }
            }
        }
    }

    companion object {
        var currentState: GamesState
            set(value) {
                StateSaver.free = value
            }
            get() = StateSaver.free
    }
}