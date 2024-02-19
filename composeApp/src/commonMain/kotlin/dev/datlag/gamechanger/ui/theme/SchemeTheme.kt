package dev.datlag.gamechanger.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.kmpalette.DominantColorState
import com.kmpalette.rememberPainterDominantColorState
import com.materialkolor.AnimatedDynamicMaterialTheme
import dev.datlag.gamechanger.LocalDarkMode
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.compose.toLegacyColors
import dev.datlag.tooling.compose.withIOContext
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

data object SchemeTheme {

    internal val commonSchemeKey = MutableStateFlow<Any?>(null)
    internal val itemScheme = MutableStateFlow<Map<Any, Color?>>(emptyMap())

    internal var _state: DominantColorState<Painter>? = null
    internal val state: DominantColorState<Painter>
        get() = _state!!

    fun setCommon(key: Any?) {
        commonSchemeKey.update { key }
    }

    @Composable
    fun update(key: Any?, input: Painter?) {
        if (_state == null || input == null) {
            return
        }

        LaunchedEffect(key, input) {
            suspendUpdate(key, input)
        }
    }

    fun update(key: Any?, input: Painter?, scope: CoroutineScope) {
        scope.launchIO {
            suspendUpdate(key, input)
        }
    }

    suspend fun suspendUpdate(key: Any?, input: Painter?) {
        if (_state == null || key == null ||  input == null) {
            return
        }

        withIOContext {
            state.updateFrom(input)

            itemScheme.getAndUpdate {
                it.toMutableMap().apply {
                    put(key, state.color)
                }
            }
        }
    }
}

@Composable
fun rememberSchemeThemeDominantColor(key: Any?): Color? {
    if (SchemeTheme._state == null) {
        SchemeTheme._state = rememberPainterDominantColorState()
    }

    val color by remember(key) {
        SchemeTheme.itemScheme.map { it[key] }
    }.collectAsStateWithLifecycle(SchemeTheme.itemScheme.value[key])

    return color
}

@Composable
fun SchemeTheme(key: Any?, content: @Composable () -> Unit) {
    AnimatedDynamicMaterialTheme(
        seedColor = rememberSchemeThemeDominantColor(key) ?: MaterialTheme.colorScheme.primary,
        useDarkTheme = LocalDarkMode.current
    ) {
        androidx.compose.material.MaterialTheme(
            colors = MaterialTheme.colorScheme.toLegacyColors(LocalDarkMode.current)
        ) {
            content()
        }
    }
}

@Composable
fun CommonSchemeTheme(content: @Composable () -> Unit) {
    val key by SchemeTheme.commonSchemeKey.collectAsStateWithLifecycle()

    SchemeTheme(key, content)
}

@Composable
expect fun SchemeThemeSystemProvider(scheme: ColorScheme, content: @Composable () -> Unit)