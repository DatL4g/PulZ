package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.pulz.common.onRenderApplyCommonScheme
import dev.datlag.pulz.hltv.model.Home
import org.kodein.di.DI

class TeamDetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val initialTeam: Home.Team,
    private val onBack: () -> Unit
) : TeamDetailsComponent, ComponentContext by componentContext {

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRenderApplyCommonScheme(initialTeam.href) {
            TeamDetailsScreen(this)
        }
    }

    override fun back() {
        onBack()
    }
}