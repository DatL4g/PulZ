package dev.datlag.gamechanger.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

actual fun <T> Flow<T>.getValueBlocking(fallback: T & Any): T & Any {
    if (this is StateFlow) {
        return this.value ?: fallback
    }

    return fallback
}