package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.common.onRenderApplyCommonScheme
import dev.datlag.gamechanger.common.onRenderWithScheme
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.tooling.compose.ioDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.kodein.di.DI
import org.kodein.di.instance

class CounterStrikeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onBack: () -> Unit
) : CounterStrikeComponent, ComponentContext by componentContext {

    override val canLaunch: Boolean = SteamLauncher.launchSupported
    override val steamGame: Game.Steam = Game.Steam.CounterStrike

    private val hltvHomeStateMachine by di.instance<HomeStateMachine>()
    override val hltvHomeState: Flow<HomeStateMachine.State> = hltvHomeStateMachine.state.flowOn(ioDispatcher())

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)

        SteamLauncher.loggedInUsers.forEach(::println)
        SteamLauncher.rootFolders.forEach(::println)
    }

    @Composable
    override fun render() {
        onRenderApplyCommonScheme(game) {
            CounterStrikeScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun dismissContent() {
        back()
    }

    override fun launch() {
        Game.Steam.CounterStrike.launch()
    }
}