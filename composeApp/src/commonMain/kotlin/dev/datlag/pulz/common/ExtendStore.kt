package dev.datlag.pulz.common

import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.rawg.model.Game
import dev.icerock.moko.resources.ImageResource

fun Game.StoreInfo.mapToIcon(): ImageResource? {
    val name = title?.ifBlank { null } ?: redirect?.ifBlank { null }
    return when {
        name == null -> null
        name.contains("playstation", ignoreCase = true) -> SharedRes.images.playstation
        name.contains("xbox", ignoreCase = true) -> SharedRes.images.xbox
        name.contains("epic games", ignoreCase = true)
                || name.contains("epicgames", ignoreCase = true) -> SharedRes.images.epic_games
        name.contains("steam", ignoreCase = true) -> SharedRes.images.steam
        name.contains("google play", ignoreCase = true)
                || name.contains("play store", ignoreCase = true) -> SharedRes.images.google_play
        name.contains("app store", ignoreCase = true) -> SharedRes.images.app_store
        name.contains("nintendo", ignoreCase = true) -> SharedRes.images.nintendo_eshop
        name.contains("gog", ignoreCase = true) -> SharedRes.images.gog
        name.contains("ubisoft", ignoreCase = true) -> SharedRes.images.ubisoft
        name.equals("ea", ignoreCase = true)
                || name.contains("electronic art", ignoreCase = true) -> SharedRes.images.ea
        else -> null
    }
}