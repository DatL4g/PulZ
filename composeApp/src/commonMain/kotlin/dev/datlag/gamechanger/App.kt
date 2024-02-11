package dev.datlag.gamechanger

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.datlag.gamechanger.ui.theme.Colors
import dev.datlag.tooling.compose.toLegacyColors
import dev.datlag.tooling.compose.toLegacyShapes
import org.kodein.di.DI

val LocalDarkMode = compositionLocalOf<Boolean> { error("No dark mode state provided") }
val LocalDI = compositionLocalOf<DI> { error("No dependency injection provided") }

@Composable
fun App(
    di: DI,
    systemDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalDarkMode provides systemDarkTheme,
        LocalDI provides di
    ) {
        MaterialTheme(
            colorScheme = if (systemDarkTheme) Colors.getDarkScheme() else Colors.getLightScheme()
        ) {
            androidx.compose.material.MaterialTheme(
                colors = MaterialTheme.colorScheme.toLegacyColors(systemDarkTheme),
                shapes = MaterialTheme.shapes.toLegacyShapes()
            ) {
                SystemProvider {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        content()
                    }
                }
            }
        }
    }
}

@Composable
expect fun SystemProvider(content: @Composable () -> Unit)