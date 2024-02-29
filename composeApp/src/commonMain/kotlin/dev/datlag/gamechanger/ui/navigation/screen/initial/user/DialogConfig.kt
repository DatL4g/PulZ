package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import kotlinx.serialization.Serializable

@Serializable
sealed class DialogConfig {

    @Serializable
    data class LibraryDetails(
        val name: String,
        val description: String,
        val website: String?,
        val version: String?,
        val openSource: Boolean
    ) : DialogConfig()

    @Serializable
    data class LicenseDetails(
        val name: String,
        val url: String?,
        val year: String?,
        val description: String
    ) : DialogConfig()
}