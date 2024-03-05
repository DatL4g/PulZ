package dev.datlag.pulz.other

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