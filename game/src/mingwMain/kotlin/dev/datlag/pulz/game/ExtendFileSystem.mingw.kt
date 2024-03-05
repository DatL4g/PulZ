package dev.datlag.pulz.game

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.posix.getenv

@OptIn(ExperimentalForeignApi::class)
actual val FileSystem.Companion.HOME: Path
    get() {
        return memScoped {
            getenv("USERPROFILE")!!.toKString().toPath(normalize = true)
        }
    }