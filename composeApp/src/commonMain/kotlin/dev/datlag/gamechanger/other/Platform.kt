package dev.datlag.gamechanger.other

import androidx.compose.runtime.Composable

expect class Platform {
    fun isInstantApp(): Boolean

    companion object {
        @Composable
        fun requestInstantAppInstall()
    }
}
