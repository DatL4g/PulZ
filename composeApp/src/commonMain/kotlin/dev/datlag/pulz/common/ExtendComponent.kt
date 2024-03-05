package dev.datlag.pulz.common

import androidx.compose.runtime.*
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import dev.datlag.pulz.LocalDI
import dev.datlag.pulz.ui.navigation.Component
import dev.datlag.pulz.ui.theme.SchemeTheme
import dev.datlag.tooling.decompose.lifecycle.LocalLifecycleOwner

/**
 * Can be placed in the Component interface again when
 * [https://github.com/JetBrains/compose-multiplatform/issues/3205](https://github.com/JetBrains/compose-multiplatform/issues/3205)
 * is fixed
 */
@Composable
fun Component.onRender(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDI provides di,
        LocalLifecycleOwner provides object : LifecycleOwner {
            override val lifecycle: Lifecycle = this@onRender.lifecycle
        }
    ) {
        content()
    }
    SideEffect {
        di.nullableFirebaseInstance()?.screen(this)
    }
}

@Composable
fun Component.onRenderWithScheme(key: Any?, content: @Composable () -> Unit) {
    onRender {
        SchemeTheme(key, content)
    }
}

@Composable
fun Component.onRenderApplyCommonScheme(key: Any?, content: @Composable () -> Unit) {
    onRenderWithScheme(key, content)

    SchemeTheme.setCommon(key)
    DisposableEffect(key) {
        onDispose {
            SchemeTheme.setCommon(null)
        }
    }
}