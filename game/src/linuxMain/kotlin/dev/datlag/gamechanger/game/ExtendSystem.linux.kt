package dev.datlag.gamechanger.game

import okio.Path
import okio.Path.Companion.toPath

actual val systemRoots: Set<Path>
    get() = setOf("/".toPath(normalize = true))