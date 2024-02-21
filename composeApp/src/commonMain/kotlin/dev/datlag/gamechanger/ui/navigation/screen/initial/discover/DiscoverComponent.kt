package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import dev.datlag.gamechanger.rawg.state.GamesStateMachine
import dev.datlag.gamechanger.ui.navigation.Component
import kotlinx.coroutines.flow.Flow

interface DiscoverComponent : Component {

    val gamesState: Flow<GamesStateMachine.State>

}