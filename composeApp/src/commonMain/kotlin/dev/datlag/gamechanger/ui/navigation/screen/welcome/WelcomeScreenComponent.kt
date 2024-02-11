package dev.datlag.gamechanger.ui.navigation.screen.welcome

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.settings.Settings
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.decompose.ioScope
import org.kodein.di.DI
import org.kodein.di.instance

class WelcomeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onFinish: () -> Unit
) : WelcomeComponent, ComponentContext by componentContext {

    private val appSettings: Settings.PlatformAppSettings by di.instance()

    init {
        ioScope().launchIO {
            appSettings.setWelcomeCompleted(true)
        }
    }

    @Composable
    override fun render() {
        onRender {
            WelcomeScreen(this)
        }
    }

    override fun finish() {
        onFinish()
    }
}