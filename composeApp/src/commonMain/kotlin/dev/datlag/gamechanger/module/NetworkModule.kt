package dev.datlag.gamechanger.module

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.ktorfitBuilder
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.rawg.RAWG
import dev.datlag.gamechanger.rawg.state.*
import io.ktor.client.*
import okio.FileSystem
import org.kodein.di.*

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
        bindSingleton<Ktorfit.Builder> {
            ktorfitBuilder {
                httpClient(instance<HttpClient>())
            }
        }
        bindSingleton<RAWG> {
            val builder = instance<Ktorfit.Builder>()
            builder.build {
                baseUrl("https://api.rawg.io/api/")
            }.create<RAWG>()
        }
        bindProvider<HomeStateMachine> {
            HomeStateMachine(instance())
        }
        bindProvider<TrendingGamesStateMachine> {
            TrendingGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
        bindProvider<TopRatedGamesStateMachine> {
            TopRatedGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
        bindProvider<ESportGamesStateMachine> {
            ESportGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
        bindProvider<OnlineCoopGamesStateMachine> {
            OnlineCoopGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
        bindProvider<SearchGamesStateMachine> {
            SearchGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
        bindProvider<FreeGamesStateMachine> {
            FreeGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
        bindProvider<MultiplayerGamesStateMachine> {
            MultiplayerGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null }
            )
        }
    }
}

expect fun ImageLoader.Builder.extendImageLoader(): ImageLoader.Builder