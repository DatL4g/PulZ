package dev.datlag.gamechanger.ui.navigation.screen.initial

import kotlinx.serialization.Serializable

@Serializable
sealed class View {
    @Serializable
    data object Discover : View()

    @Serializable
    data object Home : View()

    @Serializable
    data object User : View()
}