package dev.datlag.gamechanger.octane.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Matches(
    @SerialName("matches") val matches: List<Match> = emptyList()
)
