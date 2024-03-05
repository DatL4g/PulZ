package dev.datlag.pulz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.DelicateCoilApi
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.LifecycleOwner
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import dev.datlag.pulz.module.NetworkModule
import dev.datlag.pulz.other.ConsentInfo
import dev.datlag.pulz.other.LocalConsentInfo
import dev.datlag.pulz.other.StateSaver
import dev.datlag.pulz.ui.navigation.RootComponent
import dev.datlag.sekret.NativeLoader
import dev.datlag.tooling.Tooling
import dev.datlag.tooling.applicationTitle
import dev.datlag.tooling.async.suspendCatching
import dev.datlag.tooling.compose.getResourcesAsInputStream
import dev.datlag.tooling.compose.launchIO
import dev.datlag.tooling.compose.loadAppIcon
import dev.datlag.tooling.compose.withMainContext
import dev.datlag.tooling.decompose.lifecycle.LocalLifecycleOwner
import dev.datlag.tooling.systemProperty
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.kodein.di.DI
import org.kodein.di.instance
import java.awt.Image
import java.io.File
import javax.swing.ImageIcon

fun main(vararg args: String) {
    StateSaver.sekretLibraryLoaded = NativeLoader.loadLibrary("sekret", systemProperty("compose.application.resources.dir")?.let { File(it) })
    val di = DI {
        import(NetworkModule.di)
    }

    runWindow(di)
}

@OptIn(ExperimentalDecomposeApi::class, DelicateCoilApi::class)
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
    val imageLoader by di.instance<ImageLoader>()
    SingletonImageLoader.setUnsafe(imageLoader)

    Tooling.applicationTitle(appTitle)

    singleWindowApplication(
        state = windowState,
        title = appTitle,
        exitProcessOnExit = true
    ) {
        LifecycleController(lifecycle, windowState)
        loadAppIcons(
            this.window,
            rememberCoroutineScope(),
            SharedRes.assets.launcher,
            SharedRes.assets.png_launcher_1024,
            SharedRes.assets.png_launcher_512,
            SharedRes.assets.png_launcher_256,
            SharedRes.assets.png_launcher_128,
            SharedRes.assets.png_launcher_96,
            SharedRes.assets.png_launcher_64,
            SharedRes.assets.png_launcher_48,
            SharedRes.assets.png_launcher_32,
            SharedRes.assets.png_launcher_16,
            SharedRes.assets.ico_launcher_128,
            SharedRes.assets.ico_launcher_96,
            SharedRes.assets.ico_launcher_64,
            SharedRes.assets.ico_launcher_48,
            SharedRes.assets.ico_launcher_32,
            SharedRes.assets.ico_launcher_16,
        )

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

private fun loadAppIcons(
    window: ComposeWindow,
    scope: CoroutineScope,
    vararg assets: AssetResource
) = scope.launchIO {
    val appIcons = assets.map { async {
        getAppImage(it)
    } }.awaitAll().filterNotNull()

    withMainContext {
        Tooling.loadAppIcon(window, *appIcons.toTypedArray())
    }
}

private suspend fun getAppImage(asset: AssetResource): Image? = suspendCatching {
    val stream = suspendCatching {
        Tooling.getResourcesAsInputStream(PackageName::class, asset.filePath)
    }.getOrNull() ?: suspendCatching {
        Tooling.getResourcesAsInputStream(PackageName::class, asset.originalPath)
    }.getOrNull() ?: suspendCatching {
        asset.resourcesClassLoader.getResourceAsStream(asset.filePath)
    }.getOrNull()
    stream?.use {
        ImageIcon(it.readBytes()).image
    }
}.getOrNull()
