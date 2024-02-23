package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.rawg.state.TrendingGamesStateMachine
import dev.datlag.gamechanger.ui.navigation.Component
import kotlinx.coroutines.flow.Flow

interface DiscoverComponent : Component {

    val trendingGamesState: Flow<GamesState>
    val topRatedGamesState: Flow<GamesState>
    val eSportGamesState: Flow<GamesState>

}