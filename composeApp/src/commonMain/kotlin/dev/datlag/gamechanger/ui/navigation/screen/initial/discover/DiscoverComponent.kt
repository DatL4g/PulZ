package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.rawg.state.TrendingGamesStateMachine
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.Flow

interface DiscoverComponent : ContentHolderComponent {

    val trendingGamesState: Flow<GamesState>
    val topRatedGamesState: Flow<GamesState>
    val eSportGamesState: Flow<GamesState>
    val coopGamesState: Flow<GamesState>

    val child: Value<ChildSlot<DiscoverConfig, Component>>

    fun details(game: Game)

    fun retryTrending()
    fun retryTopRated()
    fun retryESports()
    fun retryCoop()

}