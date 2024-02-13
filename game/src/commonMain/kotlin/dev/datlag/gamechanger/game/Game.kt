package dev.datlag.gamechanger.game

sealed interface Game {
    
    val launcherOpenSupported: Boolean
    
    sealed interface Steam : Game {

        val id: String

        override val launcherOpenSupported: Boolean
            get() = true

        data object CounterStrike : Steam {
            override val id: String = "730"
        }
    }
}