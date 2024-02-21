package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.hltv.HLTV
import dev.datlag.gamechanger.settings.Settings
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.CounterStrikeScreenComponent
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.decompose.ioScope
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow
import org.kodein.di.DI
import org.kodein.di.instance

class HomeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI
) : HomeComponent, ComponentContext by componentContext {

    val appSettings: Settings.PlatformAppSettings by di.instance()
    val httpClient: HttpClient by di.instance()

    override val showWelcome: Flow<Boolean> = appSettings.showWelcome

    private val navigation = SlotNavigation<HomeConfig>()
    override val child: Value<ChildSlot<HomeConfig, Component>> = childSlot(
        source = navigation,
        serializer = HomeConfig.serializer(),
    ) { config, context ->
        when (config) {
            is HomeConfig.CounterStrike -> CounterStrikeScreenComponent(
                componentContext = context,
                di = di,
                onBack = navigation::dismiss
            )
        }
    }

    @Composable
    override fun render() {
        onRender {
            HomeScreen(this)
        }
    }

    override fun setWelcome(value: Boolean) {
        ioScope().launchIO {
            appSettings.setWelcomeCompleted(value)
        }
    }

    override fun showCounterStrike() {
        navigation.activate(HomeConfig.CounterStrike)
    }

    override fun dismissContent() {
        (child.value.child?.instance as? ContentHolderComponent)?.dismissContent()
    }
}