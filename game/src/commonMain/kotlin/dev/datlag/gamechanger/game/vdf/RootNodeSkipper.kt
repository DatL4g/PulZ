package dev.datlag.gamechanger.game.vdf

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.serializer

internal const val SkipperRootNode = "VDFSkipper"

class RootNodeSkipperDeserializationStrategy<T>(private val childSerializer: KSerializer<T>): DeserializationStrategy<T> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(SkipperRootNode) {
        element("", childSerializer.descriptor, emptyList(), false)
    }

    override fun deserialize(decoder: Decoder): T {
        return decoder.decodeStructure(descriptor) {
            decodeSerializableElement(descriptor, 0, childSerializer)
        }
    }
}

inline fun <reified T> RootNodeSkipperDeserializationStrategy() = RootNodeSkipperDeserializationStrategy<T>(serializer())