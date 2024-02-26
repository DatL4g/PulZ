package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component.hltv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.hltv.model.Home
import dev.datlag.tooling.compose.TopEndBottomStartCornerShape
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EventCover(
    event: Home.Event,
    onClick: (Home.Event) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            onClick(event)
        }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AsyncImage(
                model = event.image,
                contentDescription = event.title,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            if (event.live) {
                Text(
                    text = stringResource(SharedRes.strings.live),
                    modifier = Modifier.background(
                        color = Color.Red,
                        shape = TopEndBottomStartCornerShape(
                            baseShape = RoundedCornerShape(12.dp)
                        )
                    ).padding(vertical = 4.dp, horizontal = 8.dp).align(Alignment.TopEnd),
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            text = event.title,
            maxLines = 2,
            fontWeight = FontWeight.Bold
        )
    }
}