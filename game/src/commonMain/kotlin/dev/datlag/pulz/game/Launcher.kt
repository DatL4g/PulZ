package dev.datlag.pulz.game

interface Launcher {

    val launchSupported: Boolean

    fun launch(game: Game)

    companion object {
        fun launch(game: Game) {
            when (game) {
                is Game.Steam -> SteamLauncher.launch(game)
                else -> { }
            }
        }
    }
}