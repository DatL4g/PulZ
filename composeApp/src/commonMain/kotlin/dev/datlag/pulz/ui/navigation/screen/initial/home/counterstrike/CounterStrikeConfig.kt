package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike

import dev.datlag.pulz.hltv.model.Home
import kotlinx.serialization.Serializable

@Serializable
sealed class CounterStrikeConfig {

    @Serializable
    data class Article(val link: String) : CounterStrikeConfig()

    @Serializable
    sealed class Details : CounterStrikeConfig() {
        data class Team(val initial: Home.Team) : Details()
    }
}