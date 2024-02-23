package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.*
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.DetailsScreenComponent
import dev.datlag.tooling.compose.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kodein.di.DI
import org.kodein.di.instance

class DiscoverScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val showCounterStrike: () -> Unit
) : DiscoverComponent, ComponentContext by componentContext {

    private val trendingGamesStateMachine by di.instance<TrendingGamesStateMachine>()
    override val trendingGamesState: Flow<GamesState> = trendingGamesStateMachine.state.flowOn(ioDispatcher())

    private val topRatedGamesStateMachine by di.instance<TopRatedGamesStateMachine>()
    override val topRatedGamesState: Flow<GamesState> = topRatedGamesStateMachine.state.flowOn(ioDispatcher())

    private val eSportGamesStateMachine by di.instance<ESportGamesStateMachine>()
    override val eSportGamesState: Flow<GamesState> = eSportGamesStateMachine.state.flowOn(ioDispatcher())

    private val coopGamesStateMachine by di.instance<OnlineCoopGamesStateMachine>()
    override val coopGamesState: Flow<GamesState> = coopGamesStateMachine.state.flowOn(ioDispatcher())

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
                showCounterStrike = showCounterStrike
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
}