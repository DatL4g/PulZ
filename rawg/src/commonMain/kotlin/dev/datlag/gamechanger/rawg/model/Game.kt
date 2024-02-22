package dev.datlag.gamechanger.rawg.model

import dev.datlag.tooling.setFrom
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Game(
    @SerialName("id") val id: Int,
    @SerialName("slug") val slug: String? = null,
    @SerialName("name") val name: String,
    @SerialName("released") val released: String,
    @SerialName("tba") val tba: Boolean = false,
    @SerialName("background_image") val backgroundImage: String,
    @SerialName("rating") val rating: Float = -1F,
    @SerialName("rating_top") val ratingTop: Float = rating,
    @SerialName("short_screenshots") val screenshots: List<Screenshot> = emptyList(),
    @SerialName("genres") val genres: List<Genre> = emptyList(),
    @SerialName("platforms") val platforms: List<PlatformInfo> = emptyList(),
    @SerialName("parent_platforms") val parentPlatforms: List<PlatformInfo> = emptyList()
) {

    @Transient
    val firstPositiveScreenshot: Screenshot? = screenshots.firstOrNull {
        it.id >= 0 && it.image.isNotBlank()
    }

    @Transient
    val allPlatforms: Set<PlatformInfo.Platform> = setFrom(
        platforms,
        parentPlatforms
    ).map {
        it.platform
    }.toSet()

    @Serializable
    data class Screenshot(
        @SerialName("id") val id: Int = -1,
        @SerialName("image") val image: String
    )

    @Serializable
    data class Genre(
        @SerialName("slug") val slug: String? = null,
        @SerialName("name") val name: String? = slug
    )

    @Serializable
    data class PlatformInfo(
        @SerialName("platform") val platform: Platform
    ) {
        @Serializable
        data class Platform(
            @SerialName("id") val id: Int = -1,
            @SerialName("name") val name: String? = null
        )
    }
}
