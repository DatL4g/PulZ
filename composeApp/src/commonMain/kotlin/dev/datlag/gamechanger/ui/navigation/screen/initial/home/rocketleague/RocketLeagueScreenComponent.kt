package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.common.onRenderApplyCommonScheme
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.octane.state.EventsTodayStateMachine
import dev.datlag.gamechanger.octane.state.MatchesState
import dev.datlag.gamechanger.octane.state.MatchesTodayStateMachine
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.decompose.ioScope
import kotlinx.coroutines.flow.*
import org.kodein.di.DI
import org.kodein.di.instance

class RocketLeagueScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onBack: () -> Unit
) : RocketLeagueComponent, ComponentContext by componentContext {

    override val canLaunch: Boolean = SteamLauncher.launchSupported
    override val steamGame: Game.Steam = Game.Steam.RocketLeague

    private val eventsTodayStateMachine by di.instance<EventsTodayStateMachine>()
    override val eventsToday: StateFlow<EventsState> = eventsTodayStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = EventsTodayStateMachine.currentState
    )

    private val matchesTodayStateMachine by di.instance<MatchesTodayStateMachine>()
    override val matchesToday: StateFlow<MatchesState> = matchesTodayStateMachine.state.flowOn(
        context = ioDispatcher()
    ).stateIn(
        scope = ioScope(),
        started = SharingStarted.WhileSubscribed(),
        initialValue = MatchesTodayStateMachine.currentState
    )

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRenderApplyCommonScheme(game) {
            RocketLeagueScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun launch() {
        // ToDo("choose launcher")
    }
}