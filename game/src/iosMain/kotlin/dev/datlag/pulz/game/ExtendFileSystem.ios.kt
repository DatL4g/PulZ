package dev.datlag.pulz.game

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSHomeDirectory
import platform.posix.getpwuid
import platform.posix.getuid

@OptIn(ExperimentalForeignApi::class)
actual val FileSystem.Companion.HOME: Path
    get() {
        return memScoped {
            val uid = getuid()
            getpwuid(uid)?.pointed?.pw_dir?.toKString() ?: NSHomeDirectory()
        }.toPath(normalize = true)
    }