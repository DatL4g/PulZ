package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.match

import dev.datlag.gamechanger.octane.model.Match
import dev.datlag.gamechanger.ui.navigation.ContentHolderComponent

interface MatchDetailsComponent : ContentHolderComponent {

    val match: Match

    fun back()
    override fun dismissContent() {
        back()
    }
}