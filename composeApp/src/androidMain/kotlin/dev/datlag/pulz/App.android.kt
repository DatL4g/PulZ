package dev.datlag.pulz

import androidx.compose.runtime.Composable

@Composable
actual fun SystemProvider(content: @Composable () -> Unit) {
    content()
}