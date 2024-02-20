package dev.datlag.gamechanger.game

sealed interface Game {
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

        override fun launch() {
            SteamLauncher.launch(this)
        }

        data object CounterStrike : Steam {
            override val id: String = "730"
        }

        data object RocketLeague : Steam {
            override val id: String = "252950"
        }

        companion object {
            private const val RUN_GAME: String = "steam://rungameid/"
            private const val RUN: String = "steam://run/"
        }
    }

    sealed interface EpicGames : Game {

        override fun launch() {
            // ToDo("add launcher")
        }

        data object RocketLeague : EpicGames
    }

    sealed interface Multi : Game {
        val steam: Steam?
        val epicGames: EpicGames?

        override fun launch() {
            // ToDo("check which launcher to use")
        }

        data object RocketLeague : Multi {
            override val steam: Steam = Steam.RocketLeague
            override val epicGames: EpicGames = EpicGames.RocketLeague
        }
    }
}