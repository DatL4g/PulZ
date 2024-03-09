package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import dev.datlag.pulz.hltv.model.Team
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.common.bottomShadowBrush

@Composable
fun PlayerCard(
    player: Team.Player,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = player.image,
                contentDescription = player.name
            )
            Text(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).bottomShadowBrush(MaterialTheme.colorScheme.surfaceVariant).padding(8.dp),
                text = player.name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}