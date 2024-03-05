package dev.datlag.pulz.ui.navigation.screen.initial.discover

import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dev.datlag.pulz.rawg.model.Game
import dev.datlag.pulz.rawg.state.GamesState
import dev.datlag.pulz.rawg.state.SearchGamesStateMachine
import dev.datlag.pulz.rawg.state.TrendingGamesStateMachine
import dev.datlag.pulz.ui.navigation.Component
import dev.datlag.pulz.ui.navigation.ContentHolderComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface DiscoverComponent : ContentHolderComponent {

    val trendingGamesState: StateFlow<GamesState>
    val topRatedGamesState: StateFlow<GamesState>
    val eSportGamesState: StateFlow<GamesState>
    val coopGamesState: StateFlow<GamesState>
    val freeGamesState: StateFlow<GamesState>
    val multiplayerGamesState: StateFlow<GamesState>
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
    fun retrySearch()

}