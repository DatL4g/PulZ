package dev.datlag.pulz.rawg

import dev.datlag.pulz.model.Cacheable
import dev.datlag.pulz.rawg.model.Games
import dev.datlag.pulz.rawg.state.GamesState
import dev.datlag.pulz.rawg.state.SearchGamesStateMachine

internal data object StateSaver {
    var trending: GamesState = GamesState.Loading
    var topRated: GamesState = GamesState.Loading
    var eSports: GamesState = GamesState.Loading
    var coop: GamesState = GamesState.Loading
    var free: GamesState = GamesState.Loading
    var multiplayer: GamesState = GamesState.Loading

    var search: SearchGamesStateMachine.State = SearchGamesStateMachine.State.Waiting

    internal data object Cache {
        var trending = Cacheable<Games>()
        var topRated = Cacheable<Games>()
        var eSports = Cacheable<Games>()
        var coop = Cacheable<Games>()
        var free = Cacheable<Games>()
        var multiplayer = Cacheable<Games>()
    }
}