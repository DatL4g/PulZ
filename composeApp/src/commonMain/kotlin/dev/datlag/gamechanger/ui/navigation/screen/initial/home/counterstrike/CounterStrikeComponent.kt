package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.GameHolderComponent
import dev.datlag.gamechanger.ui.navigation.SteamGameHolderComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimePeriod

interface CounterStrikeComponent : ContentHolderComponent, SteamGameHolderComponent {

    val canLaunch: Boolean
    val hltvHomeState: Flow<HomeStateMachine.State>
    val dropsReset: StateFlow<DateTimePeriod>

    val child: Value<ChildSlot<CounterStrikeConfig, Component>>

    fun back()
    fun launch()

    override fun dismissContent() {
        back()
    }

    fun retryLoadingHLTV()
    fun showArticle(link: String)
}