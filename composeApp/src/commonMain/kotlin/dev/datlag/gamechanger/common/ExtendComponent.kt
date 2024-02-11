package dev.datlag.gamechanger.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import dev.datlag.gamechanger.LocalDI
import dev.datlag.gamechanger.ui.navigation.Component
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
}