package dev.datlag.gamechanger.game

import dev.datlag.tooling.Tooling
import dev.datlag.tooling.findSystemRoots
import dev.datlag.tooling.homeDirectory
import dev.datlag.tooling.normalize
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toOkioPath
import org.apache.commons.io.FileUtils

actual val FileSystem.Companion.DEFAULT: FileSystem
    get() = SYSTEM

actual val FileSystem.Companion.HOME: Path
    get() {
        val home = Tooling.homeDirectory() ?: FileUtils.getUserDirectory()
        return home.toOkioPath(normalize = true)
    }

actual fun Collection<Path>.normalize(): Set<Path> {
    return this.map {
        it.toFile()
    }.normalize().map {
        it.toOkioPath(normalize = true)
    }.toSet()
}