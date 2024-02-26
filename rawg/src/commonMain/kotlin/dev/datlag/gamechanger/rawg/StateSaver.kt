package dev.datlag.gamechanger.rawg

import dev.datlag.gamechanger.rawg.model.Games
import dev.datlag.gamechanger.rawg.state.GamesState
import dev.datlag.gamechanger.rawg.state.SearchGamesStateMachine

internal data object StateSaver {
    var trending: GamesState = GamesState.Loading
    var topRated: GamesState = GamesState.Loading
    var eSports: GamesState = GamesState.Loading
    var coop: GamesState = GamesState.Loading
    var free: GamesState = GamesState.Loading
    var multiplayer: GamesState = GamesState.Loading

    var search: SearchGamesStateMachine.State = SearchGamesStateMachine.State.Waiting

    internal data object Cache {
        var trending: Games? = null
        var topRated: Games? = null
        var eSports: Games? = null
        var coop: Games? = null
        var free: Games? = null
        var multiplayer: Games? = null
    }
}