package dev.datlag.pulz.octane.model

import dev.datlag.pulz.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Match(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String = id,
    @SerialName("event") val event: Event? = null,
    @SerialName("stage") val stage: Stage? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("date") val date: LocalDateTime? = null,
    @SerialName("blue") val blue: Site? = null,
    @SerialName("orange") val orange: Site? = null,
    @SerialName("games") private val _games: List<MatchGame>? = null
) {

    @Transient
    val games = _games ?: emptyList()

    @Transient
    val title: String? = event?.name?.ifBlank { null } ?: run {
        val blueTitle = blue?.title
        val orangeTitle = orange?.title

        if (!blueTitle.isNullOrBlank() && !orangeTitle.isNullOrBlank()) {
            "$blueTitle vs $orangeTitle"
        } else if (!blueTitle.isNullOrBlank()) {
            blueTitle
        } else if (!orangeTitle.isNullOrBlank()) {
            orangeTitle
        } else {
            null
        }
    }

    @Serializable
    data class MatchGame(
        @SerialName("_id") val id: String,
        @SerialName("blue") val blue: Int? = null,
        @SerialName("orange") val orange: Int? = null,
        @SerialName("duration") val duration: Int? = null,
    )
}
