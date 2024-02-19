package dev.datlag.gamechanger.module

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import dev.datlag.gamechanger.settings.ApplicationSettingsSerializer
import dev.datlag.gamechanger.settings.DataStoreAppSettings
import dev.datlag.gamechanger.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

actual object PlatformModule {

    private const val NAME = "AndroidPlatformModule"

    actual val di = DI.Module(NAME) {
        bindSingleton {
            val app: Context = instance()
            DataStoreFactory.create(
                storage = OkioStorage(
                    fileSystem = FileSystem.SYSTEM,
                    serializer = ApplicationSettingsSerializer,
                    producePath = {
                        app.filesDir.toOkioPath().resolve("datastore").resolve("app.settings")
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
    }

}