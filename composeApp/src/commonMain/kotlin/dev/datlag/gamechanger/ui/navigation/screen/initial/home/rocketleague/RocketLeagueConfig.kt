package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague

import dev.datlag.gamechanger.octane.model.Event
import kotlinx.serialization.Serializable

@Serializable
sealed class RocketLeagueConfig {

    @Serializable
    data class EventDetails(val event: Event) : RocketLeagueConfig()
}