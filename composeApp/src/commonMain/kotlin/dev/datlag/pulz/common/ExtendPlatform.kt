package dev.datlag.pulz.common

import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.rawg.model.Game
import dev.icerock.moko.resources.ImageResource

fun Game.PlatformInfo.Platform.mapToIcon(): ImageResource? {
    return when {
        name.contains("playstation", ignoreCase = true)
                || name.contains("psp", ignoreCase = true)
                || name.contains("ps vita", ignoreCase = true) -> SharedRes.images.playstation
        name.contains("pc", ignoreCase = true)
                || name.contains("windows", ignoreCase = true) -> SharedRes.images.windows
        name.contains("xbox", ignoreCase = true) -> SharedRes.images.xbox
        name.contains("Nintendo Switch", ignoreCase = true) -> SharedRes.images.nintendo_switch
        name.contains("ios", ignoreCase = true)
                || name.contains("macos", ignoreCase = true) -> SharedRes.images.ios
        name.contains("android", ignoreCase = true) -> SharedRes.images.android
        name.contains("linux", ignoreCase = true) -> SharedRes.images.linux
        name.contains("wii", ignoreCase = true) -> SharedRes.images.wii
        name.contains("nintendo 64", ignoreCase = true) -> SharedRes.images.n64
        name.contains("nes", ignoreCase = true) -> SharedRes.images.nes
        name.contains("gameboy", ignoreCase = true) -> SharedRes.images.gameboy
        name.contains("nintendo", ignoreCase = true)
                && name.contains("ds", ignoreCase = true) -> SharedRes.images.nintendo_ds
        name.contains("gamecube", ignoreCase = true) -> SharedRes.images.gamecube
        else -> null
    }
}