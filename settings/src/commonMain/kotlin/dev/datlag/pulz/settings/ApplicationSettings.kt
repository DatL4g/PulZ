package dev.datlag.pulz.settings

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class ApplicationSettings(
    @ProtoNumber(1) val welcomeCompleted: Boolean = false
)
