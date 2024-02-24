package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.StateSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class OnlineCoopGamesStateMachine(
    private val rawg: RAWG,
    private val key: String?
) : FlowReduxStateMachine<GamesState, GamesAction>(initialState = StateSaver.coop) {

    init {
        spec {
            inState<GamesState.Loading> {
                onEnterEffect {
                    StateSaver.coop = it
                }
                onEnter { state ->
                    StateSaver.Cache.coop?.let {
                        return@onEnter state.override { GamesState.Success(it) }
                    }
                    if (key == null) {
                        return@onEnter state.override { GamesState.Error }
                    }

                    val result = CatchResult.result<GamesState> {
                        GamesState.Success(
                            rawg.games(
                                key = key,
                                tags = "9"
                            ).also {
                                StateSaver.Cache.coop = it
                            }
                        )
                    }

                    state.override { result.asSuccess { GamesState.Error } }
                }
            }
            inState<GamesState.Success> {
                onEnterEffect {
                    StateSaver.coop = it
                }
            }
            inState<GamesState.Error> {
                onEnterEffect {
                    StateSaver.coop = it
                }
                on<GamesAction.Retry> { _, state ->
                    state.override { GamesState.Loading }
                }
            }
        }
    }

    companion object {
        var currentState: GamesState
            set(value) {
                StateSaver.coop = value
            }
            get() = StateSaver.coop
    }
}