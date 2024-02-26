package dev.datlag.gamechanger.rawg.model

import dev.datlag.gamechanger.rawg.common.normalizePlatform
import dev.datlag.gamechanger.rawg.common.normalizeStoreInfo
import dev.datlag.tooling.async.scopeCatching
import dev.datlag.tooling.setFrom
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.max
import kotlin.math.min

@Serializable
data class Game(
    @SerialName("id") val id: Int,
    @SerialName("slug") val slug: String,
    @SerialName("name") val name: String = slug,
    @SerialName("released") val released: String? = null,
    @SerialName("tba") val tba: Boolean = false,
    @SerialName("background_image") val backgroundImage: String? = null,
    @SerialName("rating") val rating: Float = -1F,
    @SerialName("rating_top") val ratingTop: Float = rating,
    @SerialName("short_screenshots") private val _screenshots: List<Screenshot>? = emptyList(),
    @SerialName("genres") private val _genres: List<Genre>? = emptyList(),
    @SerialName("platforms") private val _platforms: List<PlatformInfo>? = emptyList(),
    @SerialName("parent_platforms") private val _parentPlatforms: List<PlatformInfo>? = emptyList(),
    @SerialName("description") val description: String? = null,
    @SerialName("description_raw") val descriptionRaw: String? = null,
    @SerialName("website") val website: String? = null,
    @SerialName("reddit_url") val redditUrl: String? = null,
    @SerialName("reddit_name") val redditName: String? = null,
    @SerialName("reddit_description") val redditDescription: String? = null,
    @SerialName("reddit_logo") val redditLogo: String? = null,
    @SerialName("stores") private val _stores: List<StoreInfo>? = emptyList(),
    @SerialName("metacritic") private val _metacritic: Int? = -1
) {

    @Transient
    val metacritic = _metacritic ?: -1

    @Transient
    val genres = _genres ?: emptyList()

    @Transient
    val platforms = _platforms ?: emptyList()

    @Transient
    val parentPlatforms = _parentPlatforms ?: emptyList()

    @Transient
    val screenshots = _screenshots?.filterNot {
        it.image == backgroundImage
    } ?: emptyList()

    @Transient
    val allPlatforms: Set<PlatformInfo.Platform> = setFrom(
        platforms,
        parentPlatforms
    ).mapNotNull {
        it.platform
    }.toSet()

    @Transient
    val redditTitle: String? = redditName?.ifBlank { null } ?: redditUrl?.let {
        val url = scopeCatching {
            Url(it)
        }.getOrNull()
        val urlName = url?.encodedPath?.substringAfter("r/")?.substringBefore('/')?.ifBlank { null }
        urlName?.let { n ->
            "r/$n"
        } ?: url?.host?.substringAfter("www.")
    }

    @Transient
    val websiteTitle: String? = website?.let {
        val url = scopeCatching {
            Url(it)
        }.getOrNull()
        url?.host?.substringAfter("www.")
    }

    @Transient
    val hasSocials: Boolean = (!website.isNullOrBlank() && !websiteTitle.isNullOrBlank())
            || (!redditUrl.isNullOrBlank() && !redditTitle.isNullOrBlank())
            || metacritic > -1

    @Transient
    val stores: List<StoreInfo> = _stores?.mapNotNull { info ->
        if (info.isEmpty()) {
            null
        } else {
            info
        }
    }?.normalizeStoreInfo()?.toList() ?: emptyList()

    fun combine(other: Game): Game {
        return Game(
            id = this.id,
            slug = this.slug.ifBlank { other.slug },
            name = when {
                this.name.isBlank() -> other.name
                this.name == slug -> other.name
                else -> this.name
            },
            released = this.released?.ifBlank { null } ?: other.released,
            tba = this.tba || other.tba,
            backgroundImage = this.backgroundImage?.ifBlank { null } ?: other.backgroundImage,
            rating = when {
                this.rating <= 0.0F -> other.rating
                else -> this.rating
            },
            ratingTop = when {
                this.ratingTop <= 0.0F -> other.ratingTop
                this.ratingTop == this.rating -> other.ratingTop
                else -> this.ratingTop
            },
            _screenshots = setFrom(
                this.screenshots,
                other.screenshots
            ).toList(),
            _genres = setFrom(
                this.genres,
                other.genres
            ).toList(),
            _platforms = setFrom(
                this.platforms,
                other.platforms
            ).normalizePlatform().toList(),
            _parentPlatforms = setFrom(
                this.parentPlatforms,
                other.parentPlatforms
            ).normalizePlatform().toList(),
            description = this.description?.ifBlank { null } ?: other.description,
            website = this.website?.ifBlank { null } ?: other.website,
            redditUrl = this.redditUrl?.ifBlank { null } ?: other.redditUrl,
            redditName = this.redditName?.ifBlank { null } ?: other.redditName,
            redditDescription = this.redditDescription?.ifBlank { null } ?: other.redditDescription,
            redditLogo = this.redditLogo?.ifBlank { null } ?: other.redditLogo,
            _stores = setFrom(
                this.stores,
                other.stores
            ).toList(),
            descriptionRaw = descriptionRaw?.ifBlank { null } ?: other.descriptionRaw,
            _metacritic = min(max(this.metacritic, other.metacritic), 100)
        )
    }

    @Serializable
    data class Screenshot(
        @SerialName("id") val id: Int = -1,
        @SerialName("image") val image: String
    )

    @Serializable
    data class Genre(
        @SerialName("slug") val slug: String,
        @SerialName("name") val name: String = slug
    )

    @Serializable
    data class PlatformInfo(
        @SerialName("platform") val platform: Platform? = null,
        @SerialName("requirements") private val _requirements: Requirements? = null
    ) {

        @Transient
        val requirements: Requirements? = _requirements?.let {
            var min = it.minimum?.trim()
            if (min?.startsWith("Minimum:", ignoreCase = true) == true) {
                min = min?.substring(8)?.trim()
            }
            if (min?.endsWith("Additional Notes:", ignoreCase = true) == true) {
                min = min?.substring(startIndex = 0, endIndex = min!!.length - 17)?.trim()
            }

            var rec = it.recommended?.trim()
            if (rec?.startsWith("Recommended:", ignoreCase = true) == true) {
                rec = rec?.substring(12)?.trim()
            }
            if (rec?.endsWith("Additional Notes:", ignoreCase = true) == true) {
                rec = rec?.substring(startIndex = 0, endIndex = rec!!.length - 17)?.trim()
            }

            if (min.isNullOrBlank() && rec.isNullOrBlank()) {
                null
            } else {
                Requirements(
                    minimum = min?.trim(),
                    recommended = rec?.trim()
                )
            }
        }

        @Serializable
        data class Platform(
            @SerialName("id") val id: Int = -1,
            @SerialName("slug") val slug: String,
            @SerialName("name") private val _name: String = slug
        ) {
            @Transient
            val name = if (_name.equals("PC", true)) {
                "Windows"
            } else {
                _name
            }
        }

        @Serializable
        data class Requirements(
            @SerialName("minimum") val minimum: String? = null,
            @SerialName("recommended") val recommended: String? = null,
        )
    }

    @Serializable
    data class StoreInfo(
        @SerialName("id") val id: Int = -1,
        @SerialName("url") val url: String? = null,
        @SerialName("store") val store: Store? = null
    ) {

        @Transient
        val title: String? = store?.name?.ifBlank { null } ?: store?.domain?.ifBlank { null }?.let {
            val url = scopeCatching {
                Url(it)
            }.getOrNull()

            url?.host?.substringAfter("www.")
        }?.ifBlank { null } ?: url?.ifBlank { null }?.let {
            val url = scopeCatching {
                Url(it)
            }.getOrNull()

            url?.host?.substringAfter("www.")
        }?.ifBlank { null } ?: store?.domain?.ifBlank { null } ?: url?.ifBlank { null }

        @Transient
        val redirect: String? = url?.ifBlank { null } ?: store?.domain?.ifBlank { null }?.let {
            if (it.startsWith("https://", ignoreCase = true) || it.startsWith("http://", ignoreCase = true)) {
                it
            } else {
                "https://$it"
            }
        }

        internal fun isEmpty(): Boolean {
            return (store == null || store.isEmpty()) && url.isNullOrBlank()
        }

        @Serializable
        data class Store(
            @SerialName("id") val id: Int = -1,
            @SerialName("slug") val slug: String,
            @SerialName("name") val name: String = slug,
            @SerialName("domain") val domain: String? = null,
        ) {
            internal fun isEmpty(): Boolean {
                return id == -1 && name.isBlank() && domain.isNullOrBlank()
            }
        }
    }
}
