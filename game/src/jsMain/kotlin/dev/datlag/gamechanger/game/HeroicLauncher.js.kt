package dev.datlag.gamechanger.game

import kotlinx.browser.window
import okio.Path

actual object HeroicLauncher : Launcher {
    /**
     * Resolve steam root folders on any desktop target.
     *
     * Is empty on any non-desktop target.
     */
    actual val rootFolders: Set<Path> = emptySet()

    override val launchSupported: Boolean = false

    override fun launch(game: Game) {
        when (game) {
            is Game.Heroic -> {
                // ToDo("read config and get id")
                // window.location.href = "${Game.Heroic.LAUNCH}${game.id}"
            }
            is Game.Multi -> {
                game.heroic?.let(::launch)
            }
            else -> { }
        }
    }
}