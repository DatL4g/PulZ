package dev.datlag.gamechanger.game

import dev.datlag.gamechanger.game.model.steam.User
import okio.Path

/**
 * Single implementation in non-desktop targets to support open game from browser for example
 */
actual object SteamLauncher : Launcher {
    /**
     * Resolve steam root folders on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    actual val rootFolders: Set<Path> = emptySet()

    /**
     * Resolve logged-in users on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    actual val loggedInUsers: Set<User> = emptySet()

    override fun open(game: Game) { }
}