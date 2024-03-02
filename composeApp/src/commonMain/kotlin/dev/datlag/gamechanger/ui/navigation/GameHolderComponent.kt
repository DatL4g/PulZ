package dev.datlag.gamechanger.ui.navigation

import dev.datlag.gamechanger.game.Game

interface GameHolderComponent {
    val game: Game
}

interface SteamGameHolderComponent : GameHolderComponent {

    val steamGame: Game.Steam

    override val game: Game.Steam
        get() = steamGame
}

interface MultiGameHolderComponent : GameHolderComponent {

    val multiGame: Game.Multi

    override val game: Game
        get() = multiGame
}