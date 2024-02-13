package dev.datlag.gamechanger.game.common

import dev.datlag.tooling.scopeCatching
import okio.Closeable
import okio.Path
import okio.use

inline fun <T : Closeable?, R> T.useCatching(crossinline block: (T) -> R) : R? {
    return scopeCatching {
        this.use(block)
    }.getOrNull()
}

val Path.nameWithoutExtension: String
    get() = this.name.substringBeforeLast('.').ifBlank { this.name }