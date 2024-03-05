package dev.datlag.pulz.game

import dev.datlag.tooling.Platform
import dev.datlag.tooling.setFrom
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import kotlin.jvm.JvmStatic

actual object HeroicLauncher : Launcher {
    /**
     * Resolve heroic root folders on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    actual val rootFolders: Set<Path> by lazy {
        Location.getForSystem().map {
            it.resolvePath()
        }.toSet().normalize()
    }

    override val launchSupported: Boolean = true

    override fun launch(game: Game) {
        when (game) {
            is Game.Heroic -> {
                // ToDo("read config and get id")
            }
            is Game.Multi -> {
                game.heroic?.let(::launch)
            }
            else -> { }
        }
    }

    sealed interface Location : CharSequence {

        val path: String

        fun resolvePath(): Path {
            return when (this) {
                is Linux -> {
                    if (this.relativeToUserDirectory) {
                        FileSystem.HOME.resolve(path, normalize = true)
                    } else {
                        path.toPath(normalize = true)
                    }
                }
            }
        }

        override val length: Int
            get() = path.length

        override fun get(index: Int): Char {
            return path[index]
        }

        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
            return path.subSequence(startIndex, endIndex)
        }

        sealed interface Linux : Location {

            val relativeToUserDirectory: Boolean

            data object Flatpak : Linux {
                override val path: String = ".var/app/com.heroicgameslauncher.hgl"
                override val relativeToUserDirectory: Boolean = true
            }

            companion object {
                @JvmStatic
                val all = setOf(Flatpak)
            }
        }

        companion object {
            @JvmStatic
            fun getForSystem(): Set<Location> {
                return when {
                    Platform.isLinux -> Linux.all
                    Platform.isWindows -> emptySet()
                    Platform.isMacOS -> emptySet()
                    else -> setFrom(
                        Linux.all
                    )
                }
            }
        }
    }
}