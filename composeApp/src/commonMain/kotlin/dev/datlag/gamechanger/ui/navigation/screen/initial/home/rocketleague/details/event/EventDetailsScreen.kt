package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import dev.datlag.gamechanger.common.formatDayMon
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.common.shimmerPainter
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun EventDetailsScreen(component: EventDetailsComponent) {
    val padding = PaddingValues(16.dp)

    LazyColumn(
        modifier = Modifier.fillMaxSize().haze(LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        item {
            Box {
                AsyncImage(
                    model = component.event.logo,
                    contentDescription = component.event.name,
                    modifier = Modifier.fillParentMaxWidth().heightIn(max = 200.dp),
                    placeholder = shimmerPainter(),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Inside
                )
                IconButton(
                    onClick = {
                        component.back()
                    },
                    modifier = Modifier.background(
                        color = Color.Black.copy(alpha = 0.5F),
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = stringResource(SharedRes.strings.back),
                        tint = Color.White
                    )
                }
            }
        }
        item {
            Text(
                modifier = Modifier.fillParentMaxWidth(),
                text = component.event.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                maxLines = 3
            )
        }
        item {
            Row(
                modifier = Modifier.fillParentMaxWidth().padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(SharedRes.strings.prize_colon),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(SharedRes.strings.region_colon),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Today,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(SharedRes.strings.start_date_colon),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(SharedRes.strings.end_date_colon),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                    Row(
                        modifier = Modifier.height(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(SharedRes.strings.lan_colon),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = component.event.prize?.asString ?: stringResource(SharedRes.strings.no_prize)
                    )
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = component.event.region ?: stringResource(SharedRes.strings.unknown_region)
                    )
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = component.event.startDate?.formatDayMon(true) ?: stringResource(SharedRes.strings.no_date)
                    )
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = component.event.endDate?.formatDayMon(true) ?: stringResource(SharedRes.strings.no_date)
                    )
                    Text(
                        modifier = Modifier.height(24.dp),
                        text = stringResource(if (component.event.isLan) {
                            SharedRes.strings.yes
                        } else {
                            SharedRes.strings.no
                        })
                    )
                }
            }
        }
    }
}