package dev.datlag.gamechanger.rawg.state

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import dev.datlag.gamechanger.model.Cacheable
import dev.datlag.gamechanger.model.CatchResult
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.model.Game
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class GameInfoStateMachine(
    private val rawg: RAWG,
    private val key: String?,
    slug: String
) : FlowReduxStateMachine<GameInfoStateMachine.State, GameInfoStateMachine.Action>(initialState = State.Loading(slug)) {

    private var _currentState: State = State.Loading(slug)
    val currentState: State
        get() = _currentState

    private val cache = Cacheable<Game>()

    init {
        spec {
            inState<State.Loading> {
                onEnterEffect {
                    _currentState = it
                }
                onEnter { state ->
                    cache.getAlive()?.let {
                        return@onEnter state.override { State.Success(it, state.snapshot.slug) }
                    }
                    if (key == null) {
                        return@onEnter state.override {
                            State.Error(
                                canRetry = false,
                                slug = state.snapshot.slug
                            )
                        }
                    }

                    val result = CatchResult.result<State> {
                        State.Success(
                            game = rawg.game(
                                slug = state.snapshot.slug,
                                key = key
                            ).also {
                                cache.cache(it)
                            },
                            slug = state.snapshot.slug
                        )
                    }

                    state.override {
                        result.asSuccess {
                            cache.getEvenUnAlive()?.let {
                                State.Success(
                                    game = it,
                                    slug = state.snapshot.slug
                                )
                            } ?: State.Error(
                                canRetry = true,
                                slug = state.snapshot.slug
                            )
                        }
                    }
                }
            }
            inState<State.Success> {
                onEnterEffect {
                    _currentState = it
                }
            }
            inState<State.Error> {
                onEnterEffect {
                    _currentState = it
                }
                on<Action.Retry> { _, state ->
                    if (state.snapshot.canRetry) {
                        state.override {
                            State.Loading(
                                slug = state.snapshot.slug
                            )
                        }
                    } else {
                        state.noChange()
                    }
                }
            }
        }
    }

    sealed interface State {
        data class Loading(internal val slug: String) : State

        data class Success(val game: Game, internal val slug: String) : State

        data class Error(val canRetry: Boolean, internal val slug: String) : State
    }

    sealed interface Action {
        data object Retry : Action
    }
}