package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.game.SteamLauncher
import org.kodein.di.DI

class CounterStrikeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onBack: () -> Unit
) : CounterStrikeComponent, ComponentContext by componentContext {

    init {
        SteamLauncher.loggedInUsers.forEach(::println)
        SteamLauncher.rootFolders.forEach(::println)
    }

    @Composable
    override fun render() {
        onRender {
            CounterStrikeScreen(this)
        }
    }

    override fun back() {
        onBack()
    }

    override fun dismissContent() {
        back()
    }
}