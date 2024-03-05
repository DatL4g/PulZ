package dev.datlag.pulz.other

import androidx.compose.runtime.Composable

expect class Platform {
    fun isInstantApp(): Boolean

    companion object {
        @Composable
        fun requestInstantAppInstall()
    }
}
