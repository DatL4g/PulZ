package dev.datlag.pulz.module

import coil3.PlatformContext
import dev.datlag.pulz.getPackageName
import dev.datlag.pulz.other.Platform
import dev.datlag.pulz.other.StateSaver
import dev.datlag.pulz.rawg
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import org.kodein.di.instance

actual object PlatformModule {

    private const val NAME = "NativePlatformModule"

    actual val di = DI.Module(NAME) {
        bindSingleton {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        }
        bindSingleton<HttpClient> {
            HttpClient(Darwin) {
                engine {
                    configureRequest {
                        setAllowsCellularAccess(true)
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
                rawg(getPackageName())
            } else {
                ""
            }
        }

        bindSingleton<Platform> {
            Platform()
        }
    }
}