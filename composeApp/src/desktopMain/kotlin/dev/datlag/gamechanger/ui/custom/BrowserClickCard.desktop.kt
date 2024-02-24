package dev.datlag.gamechanger.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.datlag.tooling.Platform

@Composable
internal actual fun BrowserClickCardHelper(uri: String?, clicked: Boolean) {
    LaunchedEffect(clicked) {
        if (clicked && uri != null) {
            Platform.openInBrowser(uri)
        }
    }
}