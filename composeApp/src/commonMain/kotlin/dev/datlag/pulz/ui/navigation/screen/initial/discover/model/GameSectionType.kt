package dev.datlag.pulz.ui.navigation.screen.initial.discover.model

sealed interface GameSectionType {
    data object Trending : GameSectionType
    sealed interface Default : GameSectionType {
        data object TopRated : Default
        data object ESports : Default
        data object OnlineCoop : Default
        data object Free : Default
        data object Multiplayer : Default
    }
}