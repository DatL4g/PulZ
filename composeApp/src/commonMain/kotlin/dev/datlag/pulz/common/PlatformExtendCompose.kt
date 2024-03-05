package dev.datlag.pulz.common

import androidx.compose.ui.Modifier

expect fun Modifier.browserClick(
    uri: String?
): Modifier