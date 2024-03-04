package dev.datlag.gamechanger.octane.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Games(
    @SerialName("games") val games: List<Game> = emptyList()
)
