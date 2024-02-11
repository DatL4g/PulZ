package dev.datlag.gamechanger.common

import kotlinx.coroutines.flow.Flow

expect fun <T> Flow<T>.getValueBlocking(fallback: T & Any): T & Any