package dev.datlag.gamechanger.game

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import platform.posix.getenv
import platform.posix.getpwuid
import platform.posix.getuid

@OptIn(ExperimentalForeignApi::class)
actual val FileSystem.Companion.HOME: Path
    get() {
        return memScoped {
            val home = getenv("HOME")
            if (home != null) {
                home.toKString()
            } else {
                val uid = getuid()
                getpwuid(uid)?.pointed?.pw_dir!!.toKString()
            }
        }.toPath(normalize = true)
    }