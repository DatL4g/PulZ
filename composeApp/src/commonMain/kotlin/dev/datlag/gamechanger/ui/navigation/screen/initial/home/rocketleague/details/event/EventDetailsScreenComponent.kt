package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.event

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.octane.model.Event
import org.kodein.di.DI

class EventDetailsScreenComponent(
    componentContext: ComponentContext,
    override val di: DI,
    override val event: Event,
    private val onBack: () -> Unit
) : EventDetailsComponent, ComponentContext by componentContext {

    private val backCallback = BackCallback {
        back()
    }

    init {
        backHandler.register(backCallback)
    }

    @Composable
    override fun render() {
        onRender {
            EventDetailsScreen(this)
        }
    }

    override fun back() {
        onBack()
    }
}