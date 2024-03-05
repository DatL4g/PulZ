package dev.datlag.pulz.common

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import dev.datlag.tooling.Platform
import dev.datlag.tooling.compose.onClick

actual fun Modifier.browserClick(uri: String?): Modifier = composed {
    if (uri != null) {
        this.onClick {
            Platform.openInBrowser(uri)
        }
    } else {
        this
    }
}