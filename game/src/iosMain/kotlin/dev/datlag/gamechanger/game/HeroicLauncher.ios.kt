package dev.datlag.gamechanger.game

import okio.Path

actual object HeroicLauncher : Launcher {
    /**
     * Resolve heroic root folders on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    actual val rootFolders: Set<Path> = emptySet()

    override val launchSupported: Boolean = false

    override fun launch(game: Game) { }
}