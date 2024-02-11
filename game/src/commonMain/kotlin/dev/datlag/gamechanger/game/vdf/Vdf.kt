package dev.datlag.gamechanger.game.vdf

import dev.datlag.gamechanger.game.vdf.internal.VdfDecoder
import dev.datlag.gamechanger.game.vdf.internal.VdfEncoder
import dev.datlag.gamechanger.game.vdf.internal.VdfReader
import dev.datlag.gamechanger.game.vdf.internal.VdfWriter
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource

sealed class Vdf(
    internal val encodeDefaults: Boolean,
    internal val ignoreUnknownKeys: Boolean,
    internal val binaryFormat: Boolean,
    internal val readFirstInt: Boolean,
    override val serializersModule: SerializersModule
) : StringFormat {

    fun <T> decodeFromBufferedSource(
        deserializer: DeserializationStrategy<T>,
        source: BufferedSource
    ): T = VdfDecoder(
        vdf = this,
        reader = if (binaryFormat) {
            VdfReader.BinaryImpl(readFirstInt, source)
        } else {
            VdfReader.TextImpl(source)
        }
    ).decodeSerializableValue(deserializer)

    inline fun <reified T> decodeFromBufferedSource(source: BufferedSource): T = decodeFromBufferedSource(
        serializersModule.serializer(),
        source
    )

    fun <T> encodeToBufferedSink(
        serializer: SerializationStrategy<T>,
        sink: BufferedSink,
        value: T
    ) = VdfEncoder(
        vdf = this,
        writer = VdfWriter(sink)
    ).encodeSerializableValue(serializer, value)

    inline fun <reified T> encodeToBufferedSink(sink: BufferedSink, value: T) = encodeToBufferedSink(
        serializersModule.serializer(),
        sink,
        value
    )

    override fun <T> encodeToString(serializer: SerializationStrategy<T>, value: T): String {
        return Buffer().also { buffer ->
            VdfEncoder(
                vdf = this,
                writer = VdfWriter(buffer)
            ).encodeSerializableValue(serializer, value)
        }.readUtf8()
    }

    override fun <T> decodeFromString(deserializer: DeserializationStrategy<T>, string: String): T {
        return Buffer().apply { writeUtf8(string) }.let { source ->
            VdfDecoder(
                vdf = this,
                reader = if (binaryFormat) {
                    VdfReader.BinaryImpl(readFirstInt, source)
                } else {
                    VdfReader.TextImpl(source)
                }
            ).decodeSerializableValue(deserializer)
        }
    }

    companion object Default : Vdf(
        encodeDefaults = false,
        ignoreUnknownKeys = false,
        binaryFormat = false,
        readFirstInt = false,
        serializersModule = EmptySerializersModule()
    )
}

private class VdfImpl(
    encodeDefaults: Boolean,
    ignoreUnknownKeys: Boolean,
    binaryFormat: Boolean,
    readFirstInt: Boolean,
    serializersModule: SerializersModule
) : Vdf(
    encodeDefaults = encodeDefaults,
    ignoreUnknownKeys = ignoreUnknownKeys,
    binaryFormat = binaryFormat,
    readFirstInt = readFirstInt,
    serializersModule = serializersModule
)

fun Vdf(from: Vdf = Vdf, builderAction: VdfBuilder.() -> Unit): Vdf {
    return VdfBuilder(from).apply(builderAction).let { builder ->
        VdfImpl(
            encodeDefaults = builder.encodeDefaults,
            ignoreUnknownKeys = builder.ignoreUnknownKeys,
            binaryFormat = builder.binaryFormat,
            readFirstInt = builder.readFirstInt,
            serializersModule = builder.serializersModule
        )
    }
}

/**
 * Builder of the [VdfBuilder] instance provided by `Vdf` factory function.
 */
class VdfBuilder internal constructor(vdf: Vdf) {
    /**
     * Specifies whether default values of Kotlin properties should be encoded.
     */
    var encodeDefaults: Boolean = vdf.encodeDefaults

    /**
     * Specifies whether encounters of unknown properties in the input VDF should be ignored instead of throwing [Exception].
     *
     * `false` by default.
     */
    var ignoreUnknownKeys: Boolean = vdf.ignoreUnknownKeys

    /**
     * Encodes/decodes VDF by using a "binary" format (a sequence of bytes instead of a full-blown text formatting)
     */
    var binaryFormat: Boolean = vdf.binaryFormat

    /**
     * (Binary mode only) Consume first 4 bytes (int) when reading. Some binary VDFs require this (package information, for example)
     */
    var readFirstInt: Boolean = vdf.readFirstInt

    /**
     * Module with contextual and polymorphic serializers to be used in the resulting [Vdf] instance.
     */
    var serializersModule: SerializersModule = vdf.serializersModule
}