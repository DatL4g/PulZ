package dev.datlag.gamechanger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.datlag.gamechanger.module.NetworkModule
import dev.datlag.gamechanger.other.ConsentInfo
import dev.datlag.gamechanger.other.LocalConsentInfo
import dev.datlag.gamechanger.other.StateSaver
import dev.datlag.gamechanger.ui.navigation.RootComponent
import dev.datlag.sekret.NativeLoader
import dev.datlag.tooling.Tooling
import dev.datlag.tooling.applicationTitle
import dev.datlag.tooling.decompose.lifecycle.LocalLifecycleOwner
import dev.datlag.tooling.systemProperty
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import java.io.File

fun main(vararg args: String) {
    StateSaver.sekretLibraryLoaded = NativeLoader.loadLibrary("sekret", systemProperty("compose.application.resources.dir")?.let { File(it) })
    val di = DI {
        import(NetworkModule.di)
    }

    runWindow(di)
}

@OptIn(ExperimentalDecomposeApi::class)
private fun runWindow(di: DI) {
    val appTitle = StringDesc.Resource(SharedRes.strings.app_name).localized()
    val windowState = WindowState()
    val lifecycle = LifecycleRegistry()
    val lifecycleOwner = object : LifecycleOwner {
        override val lifecycle: Lifecycle = lifecycle
    }
    val backDispatcher = BackDispatcher()
    val root = RootComponent(
        componentContext = DefaultComponentContext(
            lifecycle = lifecycle,
            backHandler = backDispatcher
        ),
        di = di
    )
    val consentInfo = ConsentInfo()

    Tooling.applicationTitle(appTitle)

    singleWindowApplication(
        state = windowState,
        title = appTitle,
        exitProcessOnExit = true
    ) {
        LifecycleController(lifecycle, windowState)

        CompositionLocalProvider(
            LocalLifecycleOwner provides lifecycleOwner,
            LocalConsentInfo provides consentInfo
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