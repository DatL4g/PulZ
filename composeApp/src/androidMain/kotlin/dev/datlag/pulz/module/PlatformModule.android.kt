package dev.datlag.pulz.module

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioStorage
import dev.datlag.pulz.Sekret
import dev.datlag.pulz.common.nullableGoogleAuthProvider
import dev.datlag.pulz.firebase.*
import dev.datlag.pulz.getPackageName
import dev.datlag.pulz.other.Platform
import dev.datlag.pulz.other.StateSaver
import dev.datlag.pulz.settings.ApplicationSettingsSerializer
import dev.datlag.pulz.settings.DataStoreAppSettings
import dev.datlag.pulz.settings.Settings
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toOkioPath
import org.kodein.di.*

actual object PlatformModule {

    private const val NAME = "AndroidPlatformModule"

    actual val di = DI.Module(NAME) {
        bindSingleton {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        }
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
                install(ContentNegotiation) {
                    json(instance(), ContentType.Application.Json)
                    json(instance(), ContentType.Text.Plain)
                }
            }
        }
        bindSingleton("RAWG_API_KEY") {
            if (StateSaver.sekretLibraryLoaded) {
                Sekret.rawg(getPackageName()) ?: ""
            } else {
                ""
            }
        }

        bindSingleton<CredentialManager> {
            CredentialManager.create(instance())
        }
        bindSingleton {
            if (StateSaver.sekretLibraryLoaded) {
                GoogleAuthProvider.create(
                    credentialManager = instance(),
                    credentials = GoogleAuthCredentials(Sekret.firebaseGoogleId(getPackageName())!!)
                )
            } else {
                GoogleAuthProvider.Empty
            }
        }

        bindSingleton<FirebaseFactory> {
            if (StateSaver.sekretLibraryLoaded) {
                FirebaseFactory.initialize(
                    context = instance<Context>(),
                    projectId = Sekret.firebaseProject(getPackageName()),
                    applicationId = Sekret.firebaseAndroidApplication(getPackageName())!!,
                    apiKey = Sekret.firebaseAndroidApiKey(getPackageName())!!,
                    googleAuthProvider = nullableGoogleAuthProvider()
                )
            } else {
                FirebaseFactory.Empty
            }
        }

        bindSingleton<Platform> {
            Platform(instance())
        }
    }

}