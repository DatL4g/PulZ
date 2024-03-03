package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.match

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.common.shimmerPainter
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.match.component.StatisticSeeker
import dev.datlag.tooling.compose.TopStartBottomEndCornerShape
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun MatchDetailsScreen(component: MatchDetailsComponent) {
    val padding = PaddingValues(16.dp)

    LazyColumn(
        modifier = Modifier.fillMaxSize().haze(LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        item {
            Box(
                modifier = Modifier.fillParentMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                    ) {
                        Column(
                            modifier = Modifier.weight(1F),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = component.match.blue?.teamContainer?.team?.image,
                                modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.small),
                                contentDescription = component.match.blue?.title,
                                contentScale = ContentScale.Inside,
                                alignment = Alignment.Center,
                                placeholder = shimmerPainter()
                            )
                            component.match.blue?.score?.let {
                                Text(text = it.toString())
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1F),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = component.match.orange?.teamContainer?.team?.image,
                                modifier = Modifier.size(100.dp).clip(MaterialTheme.shapes.small),
                                contentDescription = component.match.orange?.title,
                                contentScale = ContentScale.Inside,
                                alignment = Alignment.Center,
                                placeholder = shimmerPainter()
                            )
                            component.match.orange?.score?.let {
                                Text(text = it.toString())
                            }
                        }
                    }
                }
                IconButton(
                    onClick = {
                        component.back()
                    },
                    modifier = Modifier.background(
                        color = Color.Black.copy(alpha = 0.5F),
                        shape = TopStartBottomEndCornerShape(RoundedCornerShape(12.dp))
                    ).align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(SharedRes.strings.back),
                        tint = Color.White
                    )
                }
            }
        }
        component.match.title?.let {
            item {
                Text(
                    modifier = Modifier.fillParentMaxWidth(),
                    text = it,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 3
                )
            }
        }
        item {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.statistics),
                style = MaterialTheme.typography.headlineSmall
            )
        }
        item {
            StatisticSeeker(
                blueValue = component.match.blue?.teamContainer?.stats?.core?.shots,
                orangeValue = component.match.orange?.teamContainer?.stats?.core?.shots,
                match = component.match,
                title = stringResource(SharedRes.strings.shots),
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        item {
            StatisticSeeker(
                blueValue = component.match.blue?.teamContainer?.stats?.core?.goals,
                orangeValue = component.match.orange?.teamContainer?.stats?.core?.goals,
                match = component.match,
                title = stringResource(SharedRes.strings.goals),
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        item {
            StatisticSeeker(
                blueValue = component.match.blue?.teamContainer?.stats?.core?.saves,
                orangeValue = component.match.orange?.teamContainer?.stats?.core?.saves,
                match = component.match,
                title = stringResource(SharedRes.strings.saves),
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        item {
            StatisticSeeker(
                blueValue = component.match.blue?.teamContainer?.stats?.core?.assists,
                orangeValue = component.match.orange?.teamContainer?.stats?.core?.assists,
                match = component.match,
                title = stringResource(SharedRes.strings.assists),
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        item {
            StatisticSeeker(
                blueValue = component.match.blue?.teamContainer?.stats?.core?.score,
                orangeValue = component.match.orange?.teamContainer?.stats?.core?.score,
                match = component.match,
                title = stringResource(SharedRes.strings.score),
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}