package dev.datlag.pulz.ui.navigation.screen.initial.home.rocketleague

import dev.datlag.pulz.octane.model.Event
import dev.datlag.pulz.octane.model.Match
import kotlinx.serialization.Serializable

@Serializable
sealed class RocketLeagueConfig {

    @Serializable
    data class EventDetails(val event: Event) : RocketLeagueConfig()

    @Serializable
    data class MatchDetails(val match: Match) : RocketLeagueConfig()
}