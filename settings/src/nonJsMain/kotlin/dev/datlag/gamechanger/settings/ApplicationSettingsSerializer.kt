package dev.datlag.gamechanger.settings

import androidx.datastore.core.okio.OkioSerializer
import dev.datlag.tooling.async.suspendCatching
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.protobuf.ProtoBuf
import okio.BufferedSink
import okio.BufferedSource

data object ApplicationSettingsSerializer : OkioSerializer<ApplicationSettings> {

    override val defaultValue: ApplicationSettings = ApplicationSettings()

    @OptIn(ExperimentalSerializationApi::class)
    private val protobuf = ProtoBuf {
        encodeDefaults = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun readFrom(source: BufferedSource): ApplicationSettings {
        return suspendCatching {
            protobuf.decodeFromByteArray<ApplicationSettings>(source.readByteArray())
        }.getOrNull() ?: defaultValue
    }

    @OptIn(ExperimentalSerializationApi::class)
    override suspend fun writeTo(t: ApplicationSettings, sink: BufferedSink) {
        val newSink = suspendCatching {
            sink.write(protobuf.encodeToByteArray(t))
        }.getOrNull() ?: sink

        suspendCatching {
            newSink.emit()
        }.getOrNull()
    }
}