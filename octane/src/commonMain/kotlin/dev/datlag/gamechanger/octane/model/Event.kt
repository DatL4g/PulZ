package dev.datlag.gamechanger.octane.model

import dev.datlag.gamechanger.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.*

@Serializable
data class Event(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String,
    @SerialName("name") val name: String = slug,
    @Serializable(DateTimeSerializer::class) @SerialName("startDate") val startDate: LocalDateTime? = null,
    @Serializable(DateTimeSerializer::class) @SerialName("endDate") val endDate: LocalDateTime? = null,
    @SerialName("region") val region: String? = null,
    @SerialName("mode") val mode: Int? = null,
    @SerialName("prize") val prize: Prize? = null,
    @SerialName("tier") val tier: String? = null,
    @SerialName("image") val logo: String? = null,
    @SerialName("groups") private val _groups: List<String>? = emptyList(),
    @SerialName("stages") private val _stages: List<Stage>? = emptyList()
) {

    @Transient
    val groups: Set<String> = _groups?.toSet() ?: emptySet()

    @Transient
    val stages: Set<Stage> = _stages?.toSet() ?: emptySet()

    @Serializable
    data class Prize(
        @SerialName("amount") val amount: Int? = null,
        @SerialName("currency") val currency: String? = null
    ) {
        override fun toString(): String {
            return if (amount != null && !currency.isNullOrBlank()) {
                "$amount $currency"
            } else {
                amount?.toString() ?: if (!currency.isNullOrBlank()) {
                    currency
                } else {
                    super.toString()
                }
            }
        }
    }

    @Serializable
    data class Stage(
        @SerialName("_id") val id: Int,
        @SerialName("name") val name: String? = null,
        @SerialName("format") val format: String? = null,
        @SerialName("region") val region: String? = null,
        @Serializable(DateTimeSerializer::class) @SerialName("startDate") val startDate: LocalDateTime? = null,
        @Serializable(DateTimeSerializer::class) @SerialName("endDate") val endDate: LocalDateTime? = null,
        @SerialName("prize") val prize: Prize? = null,
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
}