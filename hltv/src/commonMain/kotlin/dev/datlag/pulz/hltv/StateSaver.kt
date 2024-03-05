package dev.datlag.pulz.hltv

import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.hltv.state.HomeStateMachine

internal data object StateSaver {
    var homeState: HomeStateMachine.State = HomeStateMachine.State.Loading

    internal data object Cache {
        var home: Home? = null
    }
}