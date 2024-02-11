package dev.datlag.gamechanger.module

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.datlag.gamechanger.common.CONFIG_APP_NAME
import dev.datlag.gamechanger.settings.ApplicationSettingsSerializer
import dev.datlag.gamechanger.settings.DataStoreAppSettings
import dev.datlag.gamechanger.settings.Settings
import dev.datlag.tooling.Tooling
import dev.datlag.tooling.getRWUserDataFile
import dev.datlag.tooling.systemProperty
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

actual object PlatformModule {

    private const val NAME = "DesktopPlatformModule"

    actual val di = DI.Module(NAME) {
        systemProperty("jpackage.app-version")?.let {
            bindSingleton("APP_VERSION") {
                it
            }
        }
        bindSingleton {
            DataStoreFactory.create(
                storage = OkioStorage(
                    fileSystem = FileSystem.SYSTEM,
                    serializer = ApplicationSettingsSerializer,
                    producePath = {
                        Tooling.getRWUserDataFile(child = "app.settings", appName = Tooling.CONFIG_APP_NAME)
                            .toOkioPath()
                    }
                )
            )
        }
        bindSingleton<Settings.PlatformAppSettings> {
            DataStoreAppSettings(instance())
        }

        bindSingleton<HttpClient> {
            HttpClient(OkHttp) {
                engine {
                    config {
                        followRedirects(true)
                    }
                }
            }
        }
        bindSingleton<PlatformContext> {
            PlatformContext.INSTANCE
        }
    }

}