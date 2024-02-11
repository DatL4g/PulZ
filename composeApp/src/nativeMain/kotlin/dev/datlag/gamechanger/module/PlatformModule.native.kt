package dev.datlag.gamechanger.module

import coil3.PlatformContext
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton

actual object PlatformModule {

    private const val NAME = "NativePlatformModule"

    actual val di = DI.Module(NAME) {
        bindSingleton<HttpClient> {
            HttpClient(Darwin) {
                engine {
                    configureRequest {
                        setAllowsCellularAccess(true)
                    }
                }
            }
        }
        bindSingleton<PlatformContext> {
            PlatformContext.INSTANCE
        }
    }
}