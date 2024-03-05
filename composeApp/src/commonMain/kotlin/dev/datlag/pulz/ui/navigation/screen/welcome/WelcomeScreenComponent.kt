package dev.datlag.pulz.ui.navigation.screen.welcome

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import dev.datlag.pulz.common.onRender
import dev.datlag.pulz.settings.Settings
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.decompose.ioScope
import org.kodein.di.DI
import org.kodein.di.instance

class WelcomeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    private val onFinish: () -> Unit,
    private val onLogin: () -> Unit
) : WelcomeComponent, ComponentContext by componentContext {

    @Composable
    override fun render() {
        onRender {
            WelcomeScreen(this)
        }
    }

    override fun finish() {
        onFinish()
    }

    override fun login() {
        onLogin()
    }
}