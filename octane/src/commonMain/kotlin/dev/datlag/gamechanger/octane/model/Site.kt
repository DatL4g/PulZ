package dev.datlag.gamechanger.octane.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Site(
    @SerialName("score") private val _score: Int? = null,
    @SerialName("winner") val winner: Boolean = false,
    @SerialName("team") val teamContainer: TeamContainer? = null
) {

    @Transient
    val title: String? = teamContainer?.team?.name?.ifBlank { null }

    @Transient
    val score: Int? = _score ?: teamContainer?.stats?.core?.goals

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
}