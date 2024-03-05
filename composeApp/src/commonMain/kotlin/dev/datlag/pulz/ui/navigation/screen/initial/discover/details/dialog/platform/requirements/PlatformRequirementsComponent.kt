package dev.datlag.pulz.ui.navigation.screen.initial.discover.details.dialog.platform.requirements

import dev.datlag.pulz.rawg.model.Game
import dev.datlag.pulz.ui.navigation.DialogComponent

interface PlatformRequirementsComponent : DialogComponent {
    val platformInfo: Game.PlatformInfo
}