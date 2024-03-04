package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import kotlinx.serialization.Serializable

@Serializable
sealed class CounterStrikeConfig {

    @Serializable
    data class Article(val link: String) : CounterStrikeConfig()
}