package dev.datlag.gamechanger.game

import dev.datlag.gamechanger.game.common.nameWithoutExtension
import dev.datlag.gamechanger.game.model.steam.LibraryConfig
import dev.datlag.gamechanger.game.model.steam.User
import dev.datlag.tooling.Platform
import dev.datlag.tooling.scopeCatching
import dev.datlag.tooling.setFrom
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import okio.FileSystem
import okio.Path
import okio.Path.Companion.DIRECTORY_SEPARATOR
import okio.Path.Companion.toPath
import okio.buffer
import kotlin.jvm.JvmStatic

data object SteamLauncher : Launcher {

    val steamFolders by lazy {
        Location.getForSystem().flatMap {
            val systemPath = it.resolvePath()

            setFrom(
                setOf(
                    systemPath
                ),
                setOf(
                    systemPath.resolve("libraryfolders.vdf", normalize = true),
                    systemPath.resolve("steamapps${DIRECTORY_SEPARATOR}libraryfolders.vdf", normalize = true),
                    systemPath.resolve("config${DIRECTORY_SEPARATOR}libraryfolders.vdf", normalize = true)
                ).filter { path ->
                    FileSystem.DEFAULT.exists(path)
                }.flatMap { path ->
                    libraryConfig(path)
                }.map { config ->
                    config.path.toPath(normalize = true)
                }
            )
        }.toSet().normalize()
    }

    val loggedInUsers by lazy {
        steamFolders.asSequence().flatMap {
            listOf(
                it.resolve("config${DIRECTORY_SEPARATOR}loginusers.vdf", normalize = true),
                it.resolve("config${DIRECTORY_SEPARATOR}loggedinusers.vdf", normalize = true)
            )
        }.filter { path ->
            FileSystem.DEFAULT.exists(path)
        }.map { path ->
            userConfig(path)
        }.flatMap { config ->
            User.fromMap(config) { id ->
                findUserAvatar(id, steamFolders)
            }
        }.sortedWith(compareByDescending<User> { u ->
            u.config.mostRecent
        }.thenByDescending { u ->
            u.config.timestamp
        }).distinctBy { u -> u.id }.toList()
    }

    private val vdf = ValveDataFormat(Json {
        ignoreUnknownKeys = true
        isLenient = true
    })

    private fun libraryConfig(path: Path): Set<LibraryConfig> {
        val vdfData = scopeCatching {
            vdf.decodeStringFromBufferedSource<Map<String, LibraryConfig>>(FileSystem.DEFAULT.source(path).buffer())
        }.getOrNull()

        if (vdfData.isNullOrEmpty()) {
            val jsonData: JsonElement? = scopeCatching {
                vdf.decodeStringFromBufferedSource<JsonElement?>(FileSystem.DEFAULT.source(path).buffer())
            }.getOrNull()

            return jsonData?.jsonObject?.values?.mapNotNull {
                val jsonElement = scopeCatching {
                    it.jsonObject
                }.getOrNull() ?: it

                scopeCatching {
                    vdf.json.decodeFromJsonElement<LibraryConfig>(jsonElement)
                }.getOrNull()
            }?.toSet() ?: emptySet()
        } else {
            return vdfData.values.toSet()
        }
    }

    private fun userConfig(path: Path): Map<String, User.Config> {
        val vdfData = scopeCatching {
            vdf.decodeStringFromBufferedSource<Map<String, User.Config>>(FileSystem.DEFAULT.source(path).buffer())
        }.getOrNull() ?: emptyMap()

        return vdfData.ifEmpty {
            val jsonData: JsonElement? = scopeCatching {
                vdf.decodeStringFromBufferedSource<JsonElement?>(FileSystem.DEFAULT.source(path).buffer())
            }.getOrNull()

            if (jsonData != null) {
                scopeCatching {
                    vdf.json.decodeFromJsonElement<Map<String, User.Config>>(jsonData)
                }.getOrNull() ?: vdfData
            } else {
                vdfData
            }
        }
    }

    private fun findUserAvatar(id: String, paths: Collection<Path>): Path? {
        val cachePaths = paths.map { it.resolve("config/avatarcache") }
        val allPaths = cachePaths.flatMap {
            FileSystem.DEFAULT.listOrNull(it) ?: emptyList()
        }
        return allPaths.firstOrNull {
            it.nameWithoutExtension == id
        } ?: allPaths.firstOrNull {
            it.nameWithoutExtension.equals(id, true)
        } ?: allPaths.firstOrNull {
            it.name.startsWith(id)
        } ?: allPaths.firstOrNull {
            it.name.startsWith(id, true)
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
                is Windows -> path.toPath(normalize = true)
                is MacOS -> FileSystem.HOME.resolve(path, normalize = true)
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

            data object User : Linux {
                override val path: String = ".local/share/Steam/"
                override val relativeToUserDirectory: Boolean = true
            }

            data object UserSymlink : Linux {
                override val path: String = ".steam/steam/"
                override val relativeToUserDirectory: Boolean = true
            }

            data object Flatpak : Linux {
                override val path: String = ".var/app/com.valvesoftware.Steam/.local/share/Steam/"
                override val relativeToUserDirectory: Boolean = true
            }

            data object FlatpakSymlink : Linux {
                override val path: String = ".var/app/com.valvesoftware.Steam/.steam/steam/"
                override val relativeToUserDirectory: Boolean = true
            }

            companion object {
                @JvmStatic
                val all = mutableSetOf(User, UserSymlink, Flatpak, FlatpakSymlink)
            }
        }

        sealed interface Windows : Location {
            data object X86 : Windows {
                override val path: String = "C:\\Program Files (x86)\\Steam\\"
            }

            data object X64 : Windows {
                override val path: String = "C:\\Program Files\\Steam\\"
            }

            companion object {
                @JvmStatic
                val all = mutableSetOf(X86, X64)
            }
        }

        data object MacOS : Location {
            override val path: String = "Library/Application Support/Steam/"
        }

        companion object {
            @JvmStatic
            fun getForSystem(): Set<Location> {
                return when {
                    Platform.isLinux -> Linux.all
                    Platform.isWindows -> Windows.all
                    Platform.isMacOS -> setOf(MacOS)
                    else -> setFrom(
                        Linux.all,
                        Windows.all,
                        setOf(MacOS)
                    )
                }
            }
        }
    }
}