package dev.datlag.gamechanger.rawg

import dev.datlag.gamechanger.rawg.model.Games
import dev.datlag.gamechanger.rawg.state.GamesState

internal data object StateSaver {
    var trending: GamesState = GamesState.Loading
    var topRated: GamesState = GamesState.Loading
    var eSports: GamesState = GamesState.Loading
    var coop: GamesState = GamesState.Loading

    internal data object Cache {
        var trending: Games? = null
        var topRated: Games? = null
        var eSports: Games? = null
        var coop: Games? = null
    }
}