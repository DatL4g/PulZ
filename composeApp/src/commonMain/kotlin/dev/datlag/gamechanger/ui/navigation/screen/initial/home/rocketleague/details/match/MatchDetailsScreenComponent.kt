package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.match

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.octane.model.Match
import org.kodein.di.DI

class MatchDetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val match: Match,
    private val onBack: () -> Unit
) : MatchDetailsComponent, ComponentContext by componentContext {

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRender {
            MatchDetailsScreen(this)
        }
    }

    override fun back() {
        onBack()
    }
}