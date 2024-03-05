package dev.datlag.pulz

actual fun getPackageName(): String {
    return BuildConfig.APPLICATION_ID
}