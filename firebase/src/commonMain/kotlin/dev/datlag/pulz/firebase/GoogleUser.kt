package dev.datlag.pulz.firebase

/**
 * GoogleUser class holds most necessary fields
 */
data class GoogleUser(
    val idToken: String,
    val accessToken: String? = null
)
