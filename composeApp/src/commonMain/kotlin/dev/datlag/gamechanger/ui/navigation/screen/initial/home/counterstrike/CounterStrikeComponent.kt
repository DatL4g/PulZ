package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import dev.datlag.gamechanger.hltv.state.HomeStateMachine
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

    fun back()
    fun launch()

    override fun dismissContent() {
        back()
    }

    fun retryLoadingHLTV()
}