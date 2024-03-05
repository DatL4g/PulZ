package dev.datlag.pulz.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenConfig {

    @Serializable
    data object Home : ScreenConfig()

    @Serializable
    data object Welcome : ScreenConfig()

    @Serializable
    data object Login : ScreenConfig()
}