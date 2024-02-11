package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeConfig {
    @Serializable
    data object CounterStrike : HomeConfig()
}