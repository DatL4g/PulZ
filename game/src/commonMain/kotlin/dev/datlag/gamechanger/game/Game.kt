package dev.datlag.gamechanger.game

sealed interface Game {
    
    val launcherOpenSupported: Boolean

    fun launch()
    
    sealed interface Steam : Game {

        val id: String

        val headerUrl: String
            get() = "https://cdn.akamai.steamstatic.com/steam/apps/$id/header.jpg"

        val heroUrl: String
            get() = "https://cdn.akamai.steamstatic.com/steam/apps/$id/library_hero.jpg"

        fun getRunGameUri(): String {
            return "$RUN_GAME$id"
        }

        fun getRunUri(): String {
            return "$RUN$id"
        }

        override val launcherOpenSupported: Boolean
            get() = true

        override fun launch() {
            SteamLauncher.launch(this)
        }

        data object CounterStrike : Steam {
            override val id: String = "730"
        }

        companion object {
            private const val RUN_GAME: String = "steam://rungameid/"
            private const val RUN: String = "steam://run/"
        }
    }
}