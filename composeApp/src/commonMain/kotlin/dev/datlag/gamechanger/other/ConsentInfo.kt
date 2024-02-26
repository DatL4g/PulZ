package dev.datlag.gamechanger.other

import androidx.compose.runtime.staticCompositionLocalOf

expect class ConsentInfo {

    val privacy: Boolean

    fun initialize()
    fun reset()
    fun showPrivacyForm()
}

val LocalConsentInfo = staticCompositionLocalOf<ConsentInfo> { error("No consent info provided") }