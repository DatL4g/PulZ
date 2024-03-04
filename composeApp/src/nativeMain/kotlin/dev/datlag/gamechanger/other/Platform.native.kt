package dev.datlag.gamechanger.other

import androidx.compose.runtime.Composable

actual class Platform {
    actual fun isInstantApp(): Boolean {
        return false
    }

    actual companion object {
        @Composable
        actual fun requestInstantAppInstall() {
        }
    }

}