package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.StateSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class TopRatedGamesStateMachine(
    private val rawg: RAWG,
    private val key: String?
) : FlowReduxStateMachine<GamesState, GamesAction>(initialState = StateSaver.topRated) {

    init {
        spec {
            inState<GamesState.Loading> {
                onEnterEffect {
                    StateSaver.topRated = it
                }
                onEnter { state ->
                    if (key == null) {
                        return@onEnter state.override { GamesState.Error }
                    }

                    val result = CatchResult.result<GamesState> {
                        GamesState.Success(
                            rawg.games(
                                key = key,
                                metacritic = "95,100"
                            )
                        )
                    }

                    state.override { result.asSuccess { GamesState.Error } }
                }
            }
            inState<GamesState.Success> {
                onEnterEffect {
                    StateSaver.topRated = it
                }
            }
            inState<GamesState.Error> {
                onEnterEffect {
                    StateSaver.topRated = it
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
                StateSaver.topRated = value
            }
            get() = StateSaver.topRated
    }
}