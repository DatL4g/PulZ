package dev.datlag.gamechanger.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenConfig {

    @Serializable
    data object Home : ScreenConfig()

    @Serializable
    data object Welcome : ScreenConfig()
}