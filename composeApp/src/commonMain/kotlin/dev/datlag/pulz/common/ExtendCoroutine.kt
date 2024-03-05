package dev.datlag.pulz.common

import dev.datlag.tooling.compose.ioDispatcher
import dev.datlag.tooling.scopeCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

fun <T> Flow<T>.getValueBlocking(fallback: T & Any): T & Any {
    fun blocking(): T & Any {
        return scopeCatching {
            runBlocking(ioDispatcher()) {
                this@getValueBlocking.firstOrNull()
            }
        }.getOrNull() ?: scopeCatching {
            runBlocking {
                this@getValueBlocking.firstOrNull()
            }
        }.getOrNull() ?: fallback
    }

    if (this is StateFlow) {
        return this.value ?: blocking()
    }
    return blocking()
}