package dev.datlag.gamechanger.game

import okio.FileSystem
import okio.Path

actual val FileSystem.Companion.DEFAULT: FileSystem
    get() = SYSTEM
actual val FileSystem.Companion.HOME: Path
    get() = TODO()

actual fun Collection<Path>.normalize(): Set<Path> {
    return this.filter {
        FileSystem.DEFAULT.exists(it)
    }.toSet()
}