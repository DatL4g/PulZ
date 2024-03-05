package dev.datlag.pulz.game

import okio.Path

expect val systemRoots: Set<Path>
expect fun open(uri: String)