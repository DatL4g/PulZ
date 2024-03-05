package dev.datlag.pulz.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.model.CatchResult
import dev.datlag.pulz.rawg.RAWG
import dev.datlag.pulz.rawg.StateSaver
import dev.datlag.pulz.rawg.model.Game
import dev.datlag.pulz.rawg.model.Games
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.*

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingGamesStateMachine(
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
                    StateSaver.Cache.trending.getAlive()?.let {
                        return@onEnter state.override { GamesState.Success(it) }
                    }
                    if (key == null) {
                        return@onEnter state.override { GamesState.Error(canRetry = false) }
                    }

                    val result = CatchResult.result<GamesState> {
                        GamesState.Success(
                            rawg.games(
                                key = key,
                                dates = listOf(
                                    Clock.System.now().minus(1, DateTimeUnit.YEAR, TimeZone.currentSystemDefault()),
                                    Clock.System.now()
                                ).joinToString(separator = ",") {
                                    it.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
                                },
                                metacritic = "85,100",
                                ordering = "-released"
                            ).also {
                                StateSaver.Cache.trending.cache(it)
                            }
                        )
                    }

                    state.override {
                        result.asSuccess {
                            StateSaver.Cache.trending.getEvenUnAlive()?.let {
                                GamesState.Success(it)
                            } ?: GamesState.Error(canRetry = true)
                        }
                    }
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
                StateSaver.trending = value
            }
            get() = StateSaver.trending
    }
}