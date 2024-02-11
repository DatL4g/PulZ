package dev.datlag.gamechanger.model

sealed interface Celebrity {

    val steamId: String

    data object OhnePixel : Celebrity {
        override val steamId: String = "76561198045277210"
    }

    data object Papaplatte : Celebrity {
        override val steamId: String = "76561198013760707"
    }

    data object Trilluxe : Celebrity {
        override val steamId: String = "76561198021323440"
    }

    data object Dona : Celebrity {
        override val steamId: String = "76561198167794145"
    }

    data object RopZ : Celebrity {
        override val steamId: String = "76561197991272318"
    }
}