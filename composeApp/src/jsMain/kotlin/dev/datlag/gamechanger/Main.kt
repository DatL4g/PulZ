package dev.datlag.gamechanger

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.CanvasBasedWindow
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.*
import dev.datlag.gamechanger.module.NetworkModule
import dev.datlag.gamechanger.ui.navigation.RootComponent
import dev.datlag.tooling.decompose.lifecycle.LocalLifecycleOwner
import kotlinx.browser.document
import org.jetbrains.skiko.wasm.onWasmReady
import org.kodein.di.DI
import org.w3c.dom.Document

@OptIn(ExperimentalComposeUiApi::class, ExperimentalDecomposeApi::class)
fun main() {
    onWasmReady {
        val lifecycle = LifecycleRegistry()
        val lifecycleOwner = object : LifecycleOwner {
            override val lifecycle: Lifecycle = lifecycle
        }
        val di = DI {
            import(NetworkModule.di)
        }
        val backDispatcher = BackDispatcher()
        val root = RootComponent(
            componentContext = DefaultComponentContext(
                lifecycle = lifecycle,
                backHandler = backDispatcher
            ),
            di = di
        )

        lifecycle.attachToDocument()

        CanvasBasedWindow(canvasElementId = "ComposeTarget") {
            CompositionLocalProvider(
                LocalLifecycleOwner provides lifecycleOwner
            ) {
                App(
                    di = di
                ) {
                    PredictiveBackGestureOverlay(
                        backDispatcher = backDispatcher,
                        backIcon = { progress, _ ->
                            PredictiveBackGestureIcon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                progress = progress,
                                iconTintColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        root.render()
                    }
                }
            }
        }
    }
}

private fun LifecycleRegistry.attachToDocument() {
    fun onVisibilityChanged() {
        if (document.visibilityState == "visible") {
            resume()
        } else {
            stop()
        }
    }

    onVisibilityChanged()

    document.addEventListener(type = "visibilitychange", callback = { onVisibilityChanged() })
}

private val Document.visibilityState: String
    get() = asDynamic().visibilityState.unsafeCast<String>()