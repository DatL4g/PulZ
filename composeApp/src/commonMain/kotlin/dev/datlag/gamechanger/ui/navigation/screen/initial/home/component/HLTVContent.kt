package dev.datlag.gamechanger.ui.navigation.screen.initial.home.component

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.component.hltv.EventCover

fun LazyListScope.HLTVContent(homeState: HomeStateMachine.State) {
    when (homeState) {
        is HomeStateMachine.State.Loading -> {
            item {
                Text(text = "Loading home info")
            }
        }
        is HomeStateMachine.State.Error -> {
            item {
                Text(text = "Error loading home")
            }
        }
        is HomeStateMachine.State.Success -> {
            homeState.home.event?.let {
                item {
                    EventCover(it) {
                        println("Load event")
                    }
                }
            }
        }
    }
}