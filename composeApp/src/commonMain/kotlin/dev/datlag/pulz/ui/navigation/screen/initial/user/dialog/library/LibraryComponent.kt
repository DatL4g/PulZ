package dev.datlag.pulz.ui.navigation.screen.initial.user.dialog.library

import dev.datlag.pulz.ui.navigation.DialogComponent

interface LibraryComponent : DialogComponent {
    val name: String
    val description: String
    val website: String?
    val version: String?
    val openSource: Boolean
}