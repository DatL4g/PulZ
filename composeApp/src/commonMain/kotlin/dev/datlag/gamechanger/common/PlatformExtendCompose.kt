package dev.datlag.gamechanger.common

import androidx.compose.ui.Modifier

expect fun Modifier.browserClick(
    uri: String?
): Modifier