package dev.datlag.gamechanger.module

import coil3.PlatformContext
import dev.datlag.gamechanger.settings.DefaultAppSettings
import dev.datlag.gamechanger.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.js.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton

actual object PlatformModule {
    private const val NAME = "WebPlatformModule"

    actual val di = DI.Module(NAME) {
        bindSingleton<Settings.PlatformAppSettings> {
            DefaultAppSettings()
        }
        bindSingleton<HttpClient> {
            HttpClient(Js)
        }
        bindSingleton<PlatformContext> {
            PlatformContext.INSTANCE
        }
    }

}