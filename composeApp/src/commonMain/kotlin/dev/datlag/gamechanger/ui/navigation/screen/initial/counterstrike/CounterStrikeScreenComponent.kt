package dev.datlag.gamechanger.ui.navigation.screen.initial.counterstrike

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import org.kodein.di.DI

class CounterStrikeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onBack: () -> Unit
) : CounterStrikeComponent, ComponentContext by componentContext {

    init {
        showSteamFolder()
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
}

expect fun showSteamFolder()