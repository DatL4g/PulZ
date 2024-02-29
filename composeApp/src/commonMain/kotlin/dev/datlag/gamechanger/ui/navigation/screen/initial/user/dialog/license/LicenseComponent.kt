package dev.datlag.gamechanger.ui.navigation.screen.initial.user.dialog.license

import dev.datlag.gamechanger.ui.navigation.DialogComponent

interface LicenseComponent : DialogComponent {
    val name: String
    val url: String?
    val year: String?
    val description: String
}