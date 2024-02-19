package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CounterStrikeScreen(component: CounterStrikeComponent) {
    Column {
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