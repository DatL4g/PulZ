package dev.datlag.gamechanger.module

import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import io.ktor.client.*
import okio.FileSystem
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

data object NetworkModule {

    const val NAME = "NetworkModule"

    val di = DI.Module(NAME) {
        import(PlatformModule.di)

        bindSingleton<ImageLoader> {
            ImageLoader.Builder(instance())
                .components {
                    add(KtorNetworkFetcherFactory(instance<HttpClient>()))
                    add(SvgDecoder.Factory())
                }
                .memoryCache {
                    MemoryCache.Builder()
                        .maxSizePercent(instance())
                        .build()
                }
                .diskCache {
                    DiskCache.Builder()
                        .directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
                        .maxSizeBytes(512L * 1024 * 1024) // 512MB
                        .build()
                }
                .crossfade(true)
                .extendImageLoader()
                .build()
        }
        bindSingleton {
            HomeStateMachine(instance())
        }
    }
}

expect fun ImageLoader.Builder.extendImageLoader(): ImageLoader.Builder