package dev.datlag.gamechanger.model.serializer

import dev.datlag.tooling.async.scopeCatching
import kotlinx.datetime.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class DateTimeSerializer : KSerializer<LocalDateTime?> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", kind = PrimitiveKind.STRING)

    @OptIn(ExperimentalSerializationApi::class)
    override fun serialize(encoder: Encoder, value: LocalDateTime?) {
        if (value != null) {
            encoder.encodeNotNullMark()
            encoder.encodeString(value.toString())
        } else {
            encoder.encodeNull()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override fun deserialize(decoder: Decoder): LocalDateTime? {
        return if (decoder.decodeNotNullMark()) {
            val value = decoder.decodeString()

            scopeCatching {
                Instant.parse(value)
            }.getOrNull()?.toLocalDateTime(
                TimeZone.currentSystemDefault()
            ) ?: scopeCatching {
                value.toLocalDateTime()
            }.getOrNull() ?: scopeCatching {
                value.toLocalDate()
            }.getOrNull()?.atStartOfDayIn(
                TimeZone.currentSystemDefault()
            )?.toLocalDateTime(
                TimeZone.currentSystemDefault()
            )
        } else {
            decoder.decodeNull()
        }
    }
}