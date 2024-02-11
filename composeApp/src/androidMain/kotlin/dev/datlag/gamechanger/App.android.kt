package dev.datlag.gamechanger

import androidx.compose.runtime.Composable

@Composable
actual fun SystemProvider(content: @Composable () -> Unit) {
    content()
}