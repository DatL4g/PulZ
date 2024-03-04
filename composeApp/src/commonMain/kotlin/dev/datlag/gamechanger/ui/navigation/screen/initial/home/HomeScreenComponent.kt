package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.common.onRender
import dev.datlag.gamechanger.other.Platform
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.CounterStrikeScreenComponent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.RocketLeagueScreenComponent
import org.kodein.di.DI
import org.kodein.di.instance

class HomeScreenComponent(
    componentContext: ComponentContext,
    override val di: DI
) : HomeComponent, ComponentContext by componentContext {

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
            is HomeConfig.RocketLeague -> RocketLeagueScreenComponent(
                componentContext = context,
                di = di,
                onBack = navigation::dismiss
            )
        }
    }

    private val platform by di.instance<Platform>()
    override val isInstantApp: Boolean = platform.isInstantApp()

    @Composable
    override fun render() {
        onRender {
            HomeScreen(this)
        }
    }

    override fun showCounterStrike() {
        navigation.activate(HomeConfig.CounterStrike)
    }

    override fun showRocketLeague() {
        navigation.activate(HomeConfig.RocketLeague)
    }

    override fun dismissContent() {
        (child.value.child?.instance as? ContentHolderComponent)?.dismissContent() ?: navigation.dismiss()
    }
}