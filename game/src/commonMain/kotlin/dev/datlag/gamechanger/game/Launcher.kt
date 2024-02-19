package dev.datlag.gamechanger.game

interface Launcher {

    val launchSupported: Boolean

    fun launch(game: Game)

    companion object {
        fun launch(game: Game) {
            when (game) {
                is Game.Steam -> SteamLauncher.launch(game)
            }
        }
    }
}