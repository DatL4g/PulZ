package dev.datlag.gamechanger.hltv

import dev.datlag.gamechanger.hltv.state.HomeStateMachine

internal data object StateSaver {
    var homeState: HomeStateMachine.State = HomeStateMachine.State.Loading
}