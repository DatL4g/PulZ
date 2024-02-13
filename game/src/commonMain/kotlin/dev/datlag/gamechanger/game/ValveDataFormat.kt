package dev.datlag.gamechanger.game

import dev.datlag.gamechanger.game.common.useCatching
import dev.datlag.gamechanger.game.vdf.RootNodeSkipperDeserializationStrategy
import dev.datlag.gamechanger.game.vdf.Vdf
import dev.datlag.tooling.scopeCatching
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import okio.BufferedSource
import okio.use

/**
 * Convert Valve Data Format (vdf) to JSON.
 *
 * This got some tricky and hardly understandable regular expressions, which depend on each other with some string manipulation.
 *
 * @author Jeff Retz (DatLag)
 * @since 2022
 */
open class ValveDataFormat internal constructor(val json: Json) {

    val stringVdf = Vdf {
        ignoreUnknownKeys = json.configuration.ignoreUnknownKeys
        encodeDefaults = json.configuration.encodeDefaults
    }

    fun toJsonElement(value: String): JsonElement {
        val element = ifValidJsonElement(value)
        if (element != null) {
            return element
        }

        val keyValuePairsWithComma = "{${value.substringAfter('{')}".replace(VDF_ALL_ENDING_WITH_COMMA.toRegex()) {
            "${it.value},"
        }
        val keyValuePairsWithColon = keyValuePairsWithComma.replace(VDF_ALL_ENDING_WITH_COLON.toRegex()) {
            "${it.value}:"
        }
        val closingParenthesisWithComma = keyValuePairsWithColon.replace(VDF_ALL_ENDING_WITH_PARENTHESIS.toRegex()) {
            // the last match result is the end of the whole JSON and should not add a comma ','
            if (it.next() == null) {
                it.value
            } else {
                "${it.value},"
            }
        }
        return json.parseToJsonElement(closingParenthesisWithComma)
    }

    private fun ifValidJsonElement(value: String): JsonElement? {
        return scopeCatching {
            json.parseToJsonElement(value)
        }.getOrNull()
    }

    inline fun <reified T> decodeFromString(value: String): T {
        return scopeCatching {
            stringVdf.decodeFromString<T>(RootNodeSkipperDeserializationStrategy(), value)
        }.getOrNull() ?: json.decodeFromJsonElement(toJsonElement(value))
    }

    inline fun <reified T> decodeStringFromBufferedSource(source: BufferedSource): T {
        val data = source.peek().useCatching {
            scopeCatching {
                stringVdf.decodeFromBufferedSource<T>(RootNodeSkipperDeserializationStrategy(), it)
            }.getOrNull()
        } ?: decodeFromString(source.readUtf8())

        scopeCatching {
            source.close()
        }.getOrNull()

        return data
    }

    class Builder internal constructor(vdf: ValveDataFormat) {
        var json: Json = vdf.json
    }

    companion object Default : ValveDataFormat(
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    ) {
        /**
         * Match entries that need a comma ',' at the end.
         *
         * This matches key -> value based entries only, like: "key"    "value"
         *
         * Appending a comma results to: "key"    "value",
         */
        private const val VDF_ALL_ENDING_WITH_COMMA = "\"\\S+\"\\s+\"(\\S|[ ])*\"(?!(\\s+)?})"

        /**
         * Match entries that need a colon ':' at the end.
         *
         * This only works after appending the comma from [VDF_ALL_ENDING_WITH_COMMA] and matches the key of key - value pairs, like: "key"    "value",
         *
         * Appending a colon results to: "key":    "value",
         */
        private const val VDF_ALL_ENDING_WITH_COLON = "\"(\\S|[ ])*\"(?!([,]|\\s+(}|])))"

        /**
         * Match entries that need a comma ',' at the end.
         * This should be done last, but doesn't really matter (I guess).
         *
         * This matches parenthesis '} ]' to fix the JSON formatting (object/list separation), like: { "key1"    "value" } { "key2"    "value" }
         *
         * Appending a comma (if it's not the last entry) results to: { "key1"    "value" }, { "key2"    "value" }
         */
        private const val VDF_ALL_ENDING_WITH_PARENTHESIS = "(}|])(?!([,]|\\s+(}|])))"
    }
}

fun ValveDataFormat(from: ValveDataFormat = ValveDataFormat, builder: ValveDataFormat.Builder.() -> Unit): ValveDataFormat {
    val vdf = ValveDataFormat.Builder(from)
    vdf.builder()
    return ValveDataFormat(vdf.json)
}