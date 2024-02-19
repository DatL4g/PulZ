package dev.datlag.gamechanger.game

import okio.Path

expect val systemRoots: Set<Path>
expect fun open(uri: String)