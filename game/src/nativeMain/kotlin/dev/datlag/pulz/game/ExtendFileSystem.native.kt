package dev.datlag.pulz.game

import okio.FileSystem
import okio.Path

actual val FileSystem.Companion.DEFAULT: FileSystem
    get() = SYSTEM

actual fun Collection<Path>.normalize(): Set<Path> {
    return this.filter {
        FileSystem.DEFAULT.exists(it)
    }.toSet()
}