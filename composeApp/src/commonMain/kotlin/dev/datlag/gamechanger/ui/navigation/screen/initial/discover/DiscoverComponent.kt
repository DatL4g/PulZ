package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.rawg.state.SearchGamesStateMachine
import dev.datlag.gamechanger.rawg.state.TrendingGamesStateMachine
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DiscoverComponent : ContentHolderComponent {

    val trendingGamesState: StateFlow<GamesState>
    val topRatedGamesState: StateFlow<GamesState>
    val eSportGamesState: StateFlow<GamesState>
    val coopGamesState: StateFlow<GamesState>
    val searchGamesState: StateFlow<SearchGamesStateMachine.State>

    val child: Value<ChildSlot<DiscoverConfig, Component>>
    val searchQuery: Value<String>

    fun details(game: Game)

    fun retryTrending()
    fun retryTopRated()
    fun retryESports()
    fun retryCoop()

    fun updateSearchQuery(value: String)
    fun search(value: String)

}