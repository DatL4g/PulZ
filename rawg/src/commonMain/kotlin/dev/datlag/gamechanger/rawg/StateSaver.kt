package dev.datlag.gamechanger.rawg

import dev.datlag.gamechanger.rawg.state.GamesState

internal data object StateSaver {
    var trending: GamesState = GamesState.Loading
    var topRated: GamesState = GamesState.Loading
    var eSports: GamesState = GamesState.Loading
}