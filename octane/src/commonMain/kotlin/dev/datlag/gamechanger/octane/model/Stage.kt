package dev.datlag.gamechanger.octane.model

import dev.datlag.gamechanger.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Stage(
    @SerialName("_id") val id: Int,
    @SerialName("name") val name: String? = null,
    @SerialName("format") val format: String? = null,
    @SerialName("region") val region: String? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("startDate") val startDate: LocalDateTime? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("endDate") val endDate: LocalDateTime? = null,
    @SerialName("prize") val prize: Event.Prize? = null,
    @SerialName("lan") val isLan: Boolean = false,
    @SerialName("liquipedia") val liquipedia: String? = null,
    @SerialName("location") val location: Location? = null
) {

    @Serializable
    data class Location(
        @SerialName("venue") val venue: String? = null,
        @SerialName("city") val city: String? = null,
        @SerialName("country") val country: String? = null
    ) {
        override fun toString(): String {
            val v = venue?.ifBlank { null }
            val c = city?.ifBlank { null } ?: country?.ifBlank { null }?.uppercase()

            return if (!v.isNullOrBlank() && !c.isNullOrBlank()) {
                "$v - $c"
            } else if (!v.isNullOrBlank()) {
                v
            } else if (!c.isNullOrBlank()) {
                c
            } else {
                super.toString()
            }
        }
    }
}