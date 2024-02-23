package dev.datlag.gamechanger.ui.navigation.screen.initial.discover.model

sealed interface GameSectionType {
    data object Trending : GameSectionType
    data object Default : GameSectionType
}