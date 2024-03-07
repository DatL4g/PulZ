package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TeamDetailsScreen(component: TeamDetailsComponent) {
    Text(text = component.initialTeam.name)
}