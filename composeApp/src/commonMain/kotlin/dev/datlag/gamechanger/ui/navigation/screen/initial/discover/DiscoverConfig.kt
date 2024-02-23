package dev.datlag.gamechanger.ui.navigation.screen.initial.discover

import dev.datlag.gamechanger.rawg.model.Game
import kotlinx.serialization.Serializable

@Serializable
sealed class DiscoverConfig {
    @Serializable
    data class Details(
        val game: Game
    ) : DiscoverConfig()
}