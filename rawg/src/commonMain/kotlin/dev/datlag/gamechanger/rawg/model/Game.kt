package dev.datlag.gamechanger.rawg.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Game(
    @SerialName("id") val id: Int,
    @SerialName("slug") val slug: String? = null,
    @SerialName("name") val name: String,
    @SerialName("released") val released: String,
    @SerialName("tba") val tba: Boolean = false,
    @SerialName("background_image") val backgroundImage: String,
    @SerialName("rating") val rating: Float = -1F,
    @SerialName("rating_top") val ratingTop: Float = rating
)
