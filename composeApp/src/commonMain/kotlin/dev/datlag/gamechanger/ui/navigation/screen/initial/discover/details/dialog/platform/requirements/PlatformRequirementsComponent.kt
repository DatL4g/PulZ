package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.details.dialog.platform.requirements

import dev.datlag.gamechanger.rawg.model.Game
import dev.datlag.gamechanger.ui.navigation.DialogComponent

interface PlatformRequirementsComponent : DialogComponent {
    val platformInfo: Game.PlatformInfo
}