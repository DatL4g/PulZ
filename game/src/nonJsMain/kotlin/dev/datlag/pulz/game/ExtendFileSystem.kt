package dev.datlag.pulz.game

import okio.FileSystem
import okio.Path

expect val FileSystem.Companion.DEFAULT: FileSystem
expect val FileSystem.Companion.HOME: Path

expect fun Collection<Path>.normalize(): Set<Path>