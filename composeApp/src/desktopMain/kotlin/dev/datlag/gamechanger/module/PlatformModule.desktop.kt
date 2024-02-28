package dev.datlag.gamechanger.module

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import coil3.PlatformContext
import dev.datlag.gamechanger.Sekret
import dev.datlag.gamechanger.common.CONFIG_APP_NAME
import dev.datlag.gamechanger.firebase.FirebaseFactory
import dev.datlag.gamechanger.firebase.initialize
import dev.datlag.gamechanger.getPackageName
import dev.datlag.gamechanger.other.StateSaver
import dev.datlag.gamechanger.settings.ApplicationSettingsSerializer
import dev.datlag.gamechanger.settings.DataStoreAppSettings
import dev.datlag.gamechanger.settings.Settings
import dev.datlag.tooling.Tooling
import dev.datlag.tooling.getRWUserDataFile
import dev.datlag.tooling.systemProperty
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
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
            Json {
                ignoreUnknownKeys = true
                isLenient = true
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
                install(ContentNegotiation) {
                    json(instance(), ContentType.Application.Json)
                    json(instance(), ContentType.Text.Plain)
                }
            }
        }
        bindSingleton<PlatformContext> {
            PlatformContext.INSTANCE
        }
        bindSingleton("RAWG_API_KEY") {
            if (StateSaver.sekretLibraryLoaded) {
                Sekret.rawg(getPackageName()) ?: ""
            } else {
                ""
            }
        }
        bindSingleton<FirebaseFactory> {
            if (StateSaver.sekretLibraryLoaded) {
                FirebaseFactory.initialize(
                    projectId = Sekret.firebaseProject(getPackageName()),
                    applicationId = Sekret.firebaseAndroidApplication(getPackageName())!!,
                    apiKey = Sekret.firebaseAndroidApiKey(getPackageName())!!
                )
            } else {
                FirebaseFactory.Empty
            }
        }
    }

}