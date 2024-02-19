package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CounterStrikeScreen(component: CounterStrikeComponent) {
    Column(
        modifier = Modifier.safeDrawingPadding()
    ) {
        Text(text = "Counter Strike")

        if (component.canLaunch) {
            Button(
                onClick = {
                    component.launch()
                }
            ) {
                Text(text = "Launch")
            }
        }
    }
}