package dev.datlag.pulz.module

import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.network.ktor.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.ktorfitBuilder
import dev.datlag.pulz.common.nullableFirebaseInstance
import dev.datlag.pulz.hltv.state.HomeStateMachine
import dev.datlag.pulz.octane.Octane
import dev.datlag.pulz.octane.state.EventsTodayStateMachine
import dev.datlag.pulz.octane.state.EventsUpcomingStateMachine
import dev.datlag.pulz.octane.state.MatchesTodayStateMachine
import dev.datlag.pulz.rawg.RAWG
import dev.datlag.pulz.rawg.state.*
import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.compose.mainDispatcher
import io.github.aakira.napier.Napier
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
            HomeStateMachine(
                client = instance(),
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<TrendingGamesStateMachine> {
            TrendingGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<TopRatedGamesStateMachine> {
            TopRatedGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<ESportGamesStateMachine> {
            ESportGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<OnlineCoopGamesStateMachine> {
            OnlineCoopGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<SearchGamesStateMachine> {
            SearchGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<FreeGamesStateMachine> {
            FreeGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<MultiplayerGamesStateMachine> {
            MultiplayerGamesStateMachine(
                rawg = instance(),
                key = instanceOrNull<String>("RAWG_API_KEY")?.ifBlank { null },
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindSingleton<Octane> {
            val builder = instance<Ktorfit.Builder>()
            builder.build {
                baseUrl("https://zsr.octane.gg//")
            }.create<Octane>()
        }
        bindProvider<EventsTodayStateMachine> {
            EventsTodayStateMachine(
                octane = instance(),
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<MatchesTodayStateMachine> {
            MatchesTodayStateMachine(
                octane = instance(),
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
        bindProvider<EventsUpcomingStateMachine> {
            EventsUpcomingStateMachine(
                octane = instance(),
                crashlytics = nullableFirebaseInstance()?.crashlytics
            )
        }
    }
}

expect fun ImageLoader.Builder.extendImageLoader(): ImageLoader.Builder