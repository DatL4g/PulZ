package dev.datlag.gamechanger.ui.navigation.screen.initial.counterstrike

import dev.datlag.gamechanger.game.SteamLauncher

actual fun showSteamFolder() {
    SteamLauncher.steamAppsFolders.forEach {
        println(it)
    }
}