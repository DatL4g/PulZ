package dev.datlag.gamechanger.ui.navigation.screen.welcome.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.LocalDarkMode
import dev.datlag.gamechanger.SharedRes
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun FirstPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val res = if (LocalDarkMode.current) {
            SharedRes.images.game_day_dark
        } else {
            SharedRes.images.game_day_light
        }

        Image(
            modifier = Modifier.fillMaxWidth(0.5F),
            painter = painterResource(res),
            contentDescription = "Welcome",
            contentScale = ContentScale.FillWidth
        )

        Text("Welcome to Gamechanger")
    }
}