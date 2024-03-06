package dev.datlag.pulz.octane.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.pulz.firebase.FirebaseFactory
import dev.datlag.pulz.model.CatchResult
import dev.datlag.pulz.octane.Octane
import dev.datlag.pulz.octane.StateSaver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalCoroutinesApi::class)
class MatchesTodayStateMachine(
    private val octane: Octane,
    private val crashlytics: FirebaseFactory.Crashlytics?
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

                    val today = Clock.System.now()
                    val result = CatchResult.result<MatchesState> {
                        MatchesState.Success(
                            octane.matches(
                                before = today.plus(12.hours).toString(),
                                after = Clock.System.now().minus(3.days).toString(),
                                perPage = 20
                            ).also {
                                StateSaver.Cache.matchesToday.cache(it)
                            }
                        )
                    }

                    result.onError {
                        crashlytics?.log(it)
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