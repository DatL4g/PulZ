package dev.datlag.pulz.game

import okio.Path

expect object HeroicLauncher : Launcher {

    /**
     * Resolve heroic root folders on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    val rootFolders: Set<Path>
}