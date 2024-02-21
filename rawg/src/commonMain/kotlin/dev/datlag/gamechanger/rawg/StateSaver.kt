package dev.datlag.gamechanger.rawg

import dev.datlag.gamechanger.rawg.state.GamesStateMachine

internal data object StateSaver {
    var gamesState: GamesStateMachine.State = GamesStateMachine.State.Loading
}