package dev.datlag.gamechanger.octane.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.octane.Octane
import dev.datlag.gamechanger.octane.StateSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class MatchesTodayStateMachine(
    private val octane: Octane
) : FlowReduxStateMachine<MatchesState, MatchesAction>(initialState = currentState) {

    init {
        spec {
            inState<MatchesState.Loading> {
                onEnterEffect {
                    currentState = it
                }
                onEnter { state ->
                    StateSaver.Cache.matchesToday.getAlive()?.let {
                        return@onEnter state.override { MatchesState.Success(it) }
                    }

                    val result = CatchResult.result<MatchesState> {
                        MatchesState.Success(
                            octane.matches(
                                after = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                                perPage = 20
                            ).also {
                                StateSaver.Cache.matchesToday.cache(it)
                            }
                        )
                    }

                    state.override {
                        result.asSuccess {
                            StateSaver.Cache.matchesToday.getEvenUnAlive()?.let {
                                MatchesState.Success(it)
                            } ?: MatchesState.Error
                        }
                    }
                }
            }
            inState<MatchesState.Success> {
                onEnterEffect {
                    StateSaver.matchesToday = it
                }
            }
            inState<MatchesState.Error> {
                onEnterEffect {
                    StateSaver.matchesToday = it
                }
                on<MatchesAction.Retry> { _, state ->
                    state.override { MatchesState.Loading }
                }
            }
        }
    }

    companion object {
        var currentState: MatchesState
            set(value) {
                StateSaver.matchesToday = value
            }
            get() = StateSaver.matchesToday
    }
}