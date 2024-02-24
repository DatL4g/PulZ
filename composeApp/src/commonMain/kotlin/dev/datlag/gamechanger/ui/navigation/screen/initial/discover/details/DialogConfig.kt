package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details

import dev.datlag.gamechanger.rawg.model.Game
import kotlinx.serialization.Serializable

@Serializable
sealed class DialogConfig {

    @Serializable
    data class PlatformRequirements(
        val platformInfo: Game.PlatformInfo
    ) : DialogConfig()
}