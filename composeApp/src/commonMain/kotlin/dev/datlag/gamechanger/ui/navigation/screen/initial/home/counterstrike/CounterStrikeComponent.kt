package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import dev.datlag.gamechanger.ui.navigation.GameHolderComponent
import dev.datlag.gamechanger.ui.navigation.SteamGameHolderComponent
import kotlinx.coroutines.flow.Flow

interface CounterStrikeComponent : ContentHolderComponent, SteamGameHolderComponent {

    val canLaunch: Boolean
    val hltvHomeState: Flow<HomeStateMachine.State>

    fun back()

    fun launch()
}