package dev.datlag.pulz.ui.navigation.screen.initial.home

import kotlinx.serialization.Serializable

@Serializable
sealed class HomeConfig {
    @Serializable
    data object CounterStrike : HomeConfig()

    @Serializable
    data object RocketLeague : HomeConfig()
}