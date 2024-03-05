package dev.datlag.pulz.ui.navigation.screen.initial.home.rocketleague.details.match.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.octane.model.Game
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun GameCard(
    game: Game,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = game.blue?.score?.toString() ?: stringResource(SharedRes.strings.no_score),
                maxLines = 1
            )
            if (game.blue?.winner == true) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }
            Text(
                modifier = Modifier.weight(1F),
                text = game.map?.name?.ifBlank { null } ?: stringResource(SharedRes.strings.unknown_map),
                maxLines = 1,
                textAlign = TextAlign.Center,
                softWrap = true,
                overflow = TextOverflow.Ellipsis
            )
            if (game.orange?.winner == true) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null
                )
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }
            Text(
                text = game.orange?.score?.toString() ?: stringResource(SharedRes.strings.no_score),
                maxLines = 1
            )
        }
    }
}