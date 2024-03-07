package dev.datlag.pulz.common

import androidx.compose.runtime.Composable
import dev.datlag.pulz.LocalDarkMode
import dev.datlag.pulz.hltv.model.Home

@Composable
fun Home.Team.image(): String? {
    return if (LocalDarkMode.current) {
        this.imageDark ?: this.imageLight
    } else {
        this.imageLight ?: this.imageDark
    }
}