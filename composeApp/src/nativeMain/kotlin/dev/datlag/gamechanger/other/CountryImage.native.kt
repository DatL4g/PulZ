package dev.datlag.pulz.other

actual fun codeLocalized(code: String): String {
    val best = code.split("[-_]".toRegex()).firstOrNull()?.ifBlank { null } ?: code
    return best
}