package dev.datlag.gamechanger

actual fun getPackageName(): String {
    return BuildConfig.APPLICATION_ID
}