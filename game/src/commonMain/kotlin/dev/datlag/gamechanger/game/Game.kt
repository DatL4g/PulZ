package dev.datlag.gamechanger.game

sealed interface Game {
    
    val launcherOpenSupported: Boolean

    fun launch()
    
    sealed interface Steam : Game {

        val id: String
        val runGame: String
            get() = "steam://rungameid/"

        val run: String
            get() = "steam://run/"

        fun getRunGameUri(): String {
            return "$runGame$id"
        }

        fun getRunUri(): String {
            return "$run$id"
        }

        override val launcherOpenSupported: Boolean
            get() = true

        override fun launch() {
            SteamLauncher.launch(this)
        }

        data object CounterStrike : Steam {
            override val id: String = "730"
        }
    }
}