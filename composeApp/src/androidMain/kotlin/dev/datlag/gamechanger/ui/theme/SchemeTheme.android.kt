package dev.datlag.gamechanger.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun SchemeThemeSystemProvider(
    scheme: ColorScheme,
    content: @Composable () -> Unit
) {
    content()
}