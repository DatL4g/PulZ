package dev.datlag.pulz.other

import dev.datlag.tooling.async.scopeCatching
import java.util.Locale

actual fun codeLocalized(code: String): String {
    val best = code.split("[-_]".toRegex()).firstOrNull()?.ifBlank { null } ?: code
    return scopeCatching {
        Locale.Builder().setLanguage(best).build()
    }.getOrNull()?.country?.ifBlank { null } ?: scopeCatching {
        Locale.forLanguageTag(best)
    }.getOrNull()?.country?.ifBlank { null } ?: code
}