package dev.datlag.pulz.octane.model

import dev.datlag.pulz.model.serializer.DateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.*

@Serializable
data class Event(
    @SerialName("_id") val id: String,
    @SerialName("slug") val slug: String = id,
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

    @Transient
    val isLan = stages.any { it.isLan }

    @Serializable
    data class Prize(
        @SerialName("amount") val amount: Int? = null,
        @SerialName("currency") val currency: String? = null
    ) {

        @Transient
        val asString: String? = if (amount != null && !currency.isNullOrBlank()) {
            "$amount $currency"
        } else {
            amount?.toString() ?: if (!currency.isNullOrBlank()) {
                currency
            } else {
                null
            }
        }

        override fun toString(): String {
            return asString ?: super.toString()
        }
    }
}