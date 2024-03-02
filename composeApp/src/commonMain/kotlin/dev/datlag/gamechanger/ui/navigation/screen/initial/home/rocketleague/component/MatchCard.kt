package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.octane.model.Match
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MatchCard(match: Match) {
    Card(
        modifier = Modifier.width(200.dp),
        colors = if (match.event?.isLan == true) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            CardDefaults.cardColors()
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = match.event?.logo,
                    contentDescription = match.event?.name,
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = LocalContentColor.current.copy(alpha = 0.5F),
                            shape = MaterialTheme.shapes.extraSmall
                        )
                        .padding(2.dp)
                )
                Text(
                    text = match.title ?: stringResource(SharedRes.strings.match),
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = (match.blue?.score ?: 0).toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = match.blue?.title ?: stringResource(SharedRes.strings.blue),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = (match.orange?.score ?: 0).toString(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = match.orange?.title ?: stringResource(SharedRes.strings.orange),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true
                )
            }
        }
    }
}