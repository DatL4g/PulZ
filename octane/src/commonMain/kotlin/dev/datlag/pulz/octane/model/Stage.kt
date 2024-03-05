package dev.datlag.pulz.octane.model

import dev.datlag.pulz.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Stage(
    @SerialName("_id") val id: Int,
    @SerialName("name") val name: String? = null,
    @SerialName("qualifier") val qualifier: Boolean = false,
    @SerialName("format") val format: String? = null,
    @SerialName("region") val region: String? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("startDate") val startDate: LocalDateTime? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("endDate") val endDate: LocalDateTime? = null,
    @SerialName("prize") val prize: Event.Prize? = null,
    @SerialName("lan") val isLan: Boolean = false,
    @SerialName("liquipedia") val liquipedia: String? = null,
    @SerialName("location") val location: Location? = null
) {

    @Transient
    val title: String? = name?.ifBlank { null } ?: location?.asString?.ifBlank { null }

    @Serializable
    data class Location(
        @SerialName("venue") val venue: String? = null,
        @SerialName("city") val city: String? = null,
        @SerialName("country") val country: String? = null
    ) {

        @Transient
        val asString: String? = run {
            val v = venue?.ifBlank { null }
            val c = city?.ifBlank { null } ?: country?.ifBlank { null }?.uppercase()

            if (!v.isNullOrBlank() && !c.isNullOrBlank()) {
                "$v - $c"
            } else if (!v.isNullOrBlank()) {
                v
            } else if (!c.isNullOrBlank()) {
                c
            } else {
                null
            }
        }

        override fun toString(): String {
            return asString ?: super.toString()
        }
    }
}