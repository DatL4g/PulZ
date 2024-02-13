package dev.datlag.gamechanger.game

import okio.Path
import okio.Path.Companion.toPath

// ToDo("resolve all system roots natively")
actual val systemRoots: Set<Path>
    get() = setOf("C:".toPath(normalize = true))