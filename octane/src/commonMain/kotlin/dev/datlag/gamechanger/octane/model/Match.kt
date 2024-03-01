package dev.datlag.gamechanger.octane.model

import dev.datlag.gamechanger.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Match(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String = id,
    @SerialName("event") val event: Event? = null,
    @SerialName("stage") val stage: Stage? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("date") val date: LocalDateTime? = null,
    @SerialName("blue") val blue: Site,
    @SerialName("orange") val orange: Site,
) {
    @Serializable
    data class Site(
        @SerialName("score") val score: Int = 0,
        @SerialName("winner") val winner: Boolean = false
    )
}
