package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.StateSaver
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.model.Games
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.*

@OptIn(ExperimentalCoroutinesApi::class)
class TrendingGamesStateMachine(
    private val rawg: RAWG,
    private val key: String?
) : FlowReduxStateMachine<GamesState, GamesAction>(initialState = StateSaver.trending) {

    init {
        spec {
            inState<GamesState.Loading> {
                onEnterEffect {
                    StateSaver.trending = it
                }
                onEnter { state ->
                    if (key == null) {
                        return@onEnter state.override { GamesState.Error }
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
                            )
                        )
                    }

                    state.override { result.asSuccess { GamesState.Error } }
                }
            }
            inState<GamesState.Success> {
                onEnterEffect {
                    StateSaver.trending = it
                }
            }
            inState<GamesState.Error> {
                onEnterEffect {
                    StateSaver.trending = it
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
                StateSaver.trending = value
            }
            get() = StateSaver.trending
    }
}