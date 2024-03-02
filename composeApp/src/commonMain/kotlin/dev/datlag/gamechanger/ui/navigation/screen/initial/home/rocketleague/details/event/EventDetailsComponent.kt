package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.event

import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent

interface EventDetailsComponent : ContentHolderComponent {

    val event: Event

    fun back()
    override fun dismissContent() {
        back()
    }
}