package dev.datlag.gamechanger.common

import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.rawg.model.Game
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
        else -> null
    }
}