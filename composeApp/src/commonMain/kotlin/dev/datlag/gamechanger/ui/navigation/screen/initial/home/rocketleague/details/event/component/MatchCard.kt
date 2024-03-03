package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.event.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.shimmerPainter
import dev.datlag.gamechanger.octane.model.Match
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MatchCard(match: Match, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = match.blue?.teamContainer?.team?.image,
                modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small),
                contentDescription = match.blue?.teamContainer?.team?.name,
                contentScale = ContentScale.Inside,
                alignment = Alignment.Center,
                error = ColorPainter(MaterialTheme.colorScheme.primary),
                placeholder = shimmerPainter()
            )
            Text(
                modifier = Modifier.weight(1F),
                text = match.blue?.teamContainer?.team?.name ?: stringResource(SharedRes.strings.blue),
                color = if (match.blue?.winner == true) {
                    MaterialTheme.colorScheme.primary
                } else {
                    LocalContentColor.current
                },
                maxLines = 1,
                fontWeight = if (match.blue?.winner == true) {
                    FontWeight.Bold
                } else {
                    null
                },
                softWrap = true,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${match.blue?.score ?: 0} - ${match.orange?.score ?: 0}"
            )
            Text(
                modifier = Modifier.weight(1F),
                text = match.orange?.teamContainer?.team?.name ?: stringResource(SharedRes.strings.orange),
                color = if (match.orange?.winner == true) {
                    MaterialTheme.colorScheme.primary
                } else {
                    LocalContentColor.current
                },
                maxLines = 1,
                fontWeight = if (match.orange?.winner == true) {
                    FontWeight.Bold
                } else {
                    null
                },
                softWrap = true,
                overflow = TextOverflow.Ellipsis
            )
            AsyncImage(
                model = match.orange?.teamContainer?.team?.image,
                modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small),
                contentDescription = match.orange?.teamContainer?.team?.name,
                contentScale = ContentScale.Inside,
                alignment = Alignment.Center,
                error = ColorPainter(MaterialTheme.colorScheme.secondary),
                placeholder = shimmerPainter()
            )
        }
    }
}