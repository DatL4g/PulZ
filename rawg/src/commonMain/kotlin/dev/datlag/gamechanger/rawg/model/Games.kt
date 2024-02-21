package dev.datlag.gamechanger.rawg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Games(
    @SerialName("count") val count: Int = 0,
    @SerialName("next") val next: String? = null,
    @SerialName("previous") val previous: String? = null,
    @SerialName("results") val results: List<Game> = emptyList()
)
