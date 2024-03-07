package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team

import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.ui.navigation.ContentHolderComponent

interface TeamDetailsComponent : ContentHolderComponent {

    val initialTeam: Home.Team

    fun back()
    override fun dismissContent() {
        back()
    }
}