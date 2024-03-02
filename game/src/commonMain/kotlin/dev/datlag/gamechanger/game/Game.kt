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

    sealed interface Heroic : Game {

        override fun launch() {
            // ToDo("add launcher")
        }

        data object RocketLeague : Heroic
    }

    sealed class Multi : Game {
        abstract val steam: Steam?
        abstract val epicGames: EpicGames?
        abstract val heroic: Heroic?
        
        val headerUrl: String?
            get() = steam?.headerUrl

        val heroUrl: String?
            get() = steam?.heroUrl

        override fun launch() {
            // ToDo("check which launcher to use")
        }

        override fun equals(other: Any?): Boolean {
            return when (other) {
                is Steam -> this.steam == other
                is EpicGames -> this.epicGames == other
                is Heroic -> this.heroic == other
                else -> false
            } || super.equals(other)
        }

        data object RocketLeague : Multi() {
            override val steam: Steam = Steam.RocketLeague
            override val epicGames: EpicGames = EpicGames.RocketLeague
            override val heroic: Heroic = Heroic.RocketLeague
        }
    }
}