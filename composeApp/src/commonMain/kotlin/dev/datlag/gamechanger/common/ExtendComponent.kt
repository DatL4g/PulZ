package dev.datlag.gamechanger.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import dev.datlag.gamechanger.LocalDI
import dev.datlag.gamechanger.ui.navigation.Component
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.tooling.decompose.lifecycle.LocalLifecycleOwner
import org.kodein.di.instance

/**
 * Can be placed in the Component interface again when
 * [https://github.com/JetBrains/compose-multiplatform/issues/3205](https://github.com/JetBrains/compose-multiplatform/issues/3205)
 * is fixed
 */
@OptIn(DelicateCoilApi::class)
@Composable
fun Component.onRender(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalDI provides di,
        LocalLifecycleOwner provides object : LifecycleOwner {
            override val lifecycle: Lifecycle = this@onRender.lifecycle
        }
    ) {
        LaunchedEffect(di) {
            val loader by di.instance<ImageLoader>()
            SingletonImageLoader.setUnsafe(loader)
        }
        content()
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