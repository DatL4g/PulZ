package dev.datlag.gamechanger.game

import dev.datlag.gamechanger.game.model.steam.User
import okio.Path

/**
 * Single implementation in non-desktop targets to support open game from browser for example
 */
expect object SteamLauncher : Launcher {

    /**
     * Resolve steam root folders on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    val rootFolders: Set<Path>

    /**
     * Resolve logged-in users on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    val loggedInUsers: Set<User>
}