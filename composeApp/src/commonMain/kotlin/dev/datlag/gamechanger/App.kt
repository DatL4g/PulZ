package dev.datlag.gamechanger

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.datlag.gamechanger.ui.theme.Colors
import dev.datlag.gamechanger.ui.theme.CommonSchemeTheme
import dev.datlag.tooling.compose.toLegacyColors
import dev.datlag.tooling.compose.toLegacyShapes
import org.kodein.di.DI

val LocalDarkMode = compositionLocalOf<Boolean> { error("No dark mode state provided") }
val LocalDI = compositionLocalOf<DI> { error("No dependency injection provided") }
val LocalHaze = compositionLocalOf<HazeState> { error("No Haze state provided") }
val LocalPaddingValues = compositionLocalOf<PaddingValues?> { null }

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
                    CommonSchemeTheme {
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
}

@Composable
expect fun SystemProvider(content: @Composable () -> Unit)