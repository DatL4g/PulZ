package dev.datlag.gamechanger.octane.model

import dev.datlag.gamechanger.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    @SerialName("_id") val id: String,
    @SerialName("map") val map: Map? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("date") val date: LocalDateTime? = null,
    @SerialName("blue") val blue: Site? = null,
    @SerialName("orange") val orange: Site? = null
) {

    @Serializable
    data class Map(
        @SerialName("id") val id: String? = null,
        @SerialName("name") val name: String? = null
    )
}
