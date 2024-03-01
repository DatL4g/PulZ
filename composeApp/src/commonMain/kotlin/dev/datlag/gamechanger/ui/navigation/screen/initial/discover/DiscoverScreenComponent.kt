package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.*
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.DetailsScreenComponent
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import kotlinx.coroutines.flow.*
import org.kodein.di.DI
import org.kodein.di.instance

class DiscoverScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val showCounterStrike: () -> Unit,
    private val showRocketLeague: () -> Unit
) : DiscoverComponent, ComponentContext by componentContext {

    private val trendingGamesStateMachine by di.instance<TrendingGamesStateMachine>()
    override val trendingGamesState: StateFlow<GamesState> = trendingGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = TrendingGamesStateMachine.currentState
    )

    private val topRatedGamesStateMachine by di.instance<TopRatedGamesStateMachine>()
    override val topRatedGamesState: StateFlow<GamesState> = topRatedGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = TopRatedGamesStateMachine.currentState
    )

    private val eSportGamesStateMachine by di.instance<ESportGamesStateMachine>()
    override val eSportGamesState: StateFlow<GamesState> = eSportGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = ESportGamesStateMachine.currentState
    )

    private val coopGamesStateMachine by di.instance<OnlineCoopGamesStateMachine>()
    override val coopGamesState: StateFlow<GamesState> = coopGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = OnlineCoopGamesStateMachine.currentState
    )

    private val freeGamesStateMachine by di.instance<FreeGamesStateMachine>()
    override val freeGamesState: StateFlow<GamesState> = freeGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = FreeGamesStateMachine.currentState
    )

    private val multiplayerGamesStateMachine by di.instance<MultiplayerGamesStateMachine>()
    override val multiplayerGamesState: StateFlow<GamesState> = multiplayerGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = MultiplayerGamesStateMachine.currentState
    )

    private val searchGamesStateMachine by di.instance<SearchGamesStateMachine>()
    override val searchGamesState: StateFlow<SearchGamesStateMachine.State> = searchGamesStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = SearchGamesStateMachine.currentState
    )

    override val searchQuery: MutableValue<String> = MutableValue("")

    private val navigation = SlotNavigation<DiscoverConfig>()
    override val child: Value<ChildSlot<DiscoverConfig, Component>> = childSlot(
        source = navigation,
        serializer = DiscoverConfig.serializer()
    ) { config, context ->
        when (config) {
            is DiscoverConfig.Details -> DetailsScreenComponent(
                componentContext = context,
                di = di,
                initialGame = config.game,
                onBack = navigation::dismiss,
                showCounterStrike = showCounterStrike,
                showRocketLeague = showRocketLeague
            )
        }
    }

    @Composable
    override fun render() {
        onRender {
            DiscoverScreen(this)
        }
    }

    override fun dismissContent() {
        (child.value.child?.instance as? ContentHolderComponent)?.dismissContent() ?: navigation.dismiss()
    }

    override fun details(game: Game) {
        navigation.activate(DiscoverConfig.Details(game))
    }

    override fun retryTrending() {
        launchIO {
            trendingGamesStateMachine.dispatch(GamesAction.Retry)
        }
    }

    override fun retryTopRated() {
        launchIO {
            topRatedGamesStateMachine.dispatch(GamesAction.Retry)
        }
    }

    override fun retryESports() {
        launchIO {
            eSportGamesStateMachine.dispatch(GamesAction.Retry)
        }
    }

    override fun retryCoop() {
        launchIO {
            coopGamesStateMachine.dispatch(GamesAction.Retry)
        }
    }

    override fun updateSearchQuery(value: String) {
        searchQuery.value = value

        if (value.isBlank()) {
            launchIO {
                searchGamesStateMachine.dispatch(SearchGamesStateMachine.Action.Clear)
            }
        }
    }

    override fun search(value: String) {
        updateSearchQuery(value)

        if (value.isNotBlank()) {
            launchIO {
                searchGamesStateMachine.dispatch(SearchGamesStateMachine.Action.Load(value))
            }
        }
    }
}