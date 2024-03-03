package dev.datlag.gamechanger.octane.model

import dev.datlag.gamechanger.model.serializer.DateTimeSerializer
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
) {

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
    data class Site(
        @SerialName("score") val score: Int = 0,
        @SerialName("winner") val winner: Boolean = false,
        @SerialName("team") val teamContainer: TeamContainer? = null
    ) {

        @Transient
        val title: String? = teamContainer?.team?.name?.ifBlank { null }
    }

    @Serializable
    data class TeamContainer(
        @SerialName("team") val team: Team? = null,
        @SerialName("stats") val stats: StatsContainer? = null
    ) {
        @Serializable
        data class Team(
            @SerialName("_id") val id: String,
            @SerialName("slug") val slug: String = id,
            @SerialName("name") val name: String = slug,
            @SerialName("region") val region: String? = null,
            @SerialName("image") val image: String? = null,
            @SerialName("relevant") val relevant: Boolean = false,
        )
    }

    @Serializable
    data class StatsContainer(
        @SerialName("core") val core: Core? = null
    ) {

        @Serializable
        data class Core(
            @SerialName("shots") val shots: Int? = null,
            @SerialName("goals") val goals: Int? = null,
            @SerialName("saves") val saves: Int? = null,
            @SerialName("assists") val assists: Int? = null,
            @SerialName("score") val score: Int? = null,
            @SerialName("shootingPercentage") val shootingPercentage: Float? = null,
        )
    }
}
