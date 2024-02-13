package dev.datlag.gamechanger.game

import dev.datlag.tooling.Tooling
import dev.datlag.tooling.findSystemRoots
import okio.Path
import okio.Path.Companion.toOkioPath

actual val systemRoots: Set<Path>
    get() = Tooling.findSystemRoots().map {
        it.toOkioPath(normalize = true)
    }.toSet()