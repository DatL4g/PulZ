package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.common.shimmer
import dev.datlag.gamechanger.common.shimmerPainter
import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.ui.navigation.screen.initial.component.ErrorContent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.component.*
import dev.datlag.tooling.compose.TopStartBottomEndCornerShape
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun RocketLeagueScreen(component: RocketLeagueComponent) {
    val childState by component.child.subscribeAsState()

    childState.child?.instance?.render() ?: MainView(component)
}

@Composable
private fun MainView(component: RocketLeagueComponent) {
    val padding = PaddingValues(16.dp)
    val events by component.eventsToday.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize().haze(LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = component.multiGame.heroUrl,
                        contentDescription = stringResource(SharedRes.strings.rocket_league),
                        contentScale = ContentScale.FillWidth,
                        placeholder = shimmerPainter()
                    )
                    IconButton(
                        onClick = {
                            component.back()
                        },
                        modifier = Modifier.background(
                            color = Color.Black.copy(alpha = 0.5F),
                            shape = TopStartBottomEndCornerShape(RoundedCornerShape(12.dp))
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
        }
        item {
            Text(
                text = stringResource(SharedRes.strings.recent_matches),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            MatchOverview(
                matchesState = component.matchesToday,
                modifier = Modifier.fillParentMaxWidth(),
                retry = {
                    component.retryLoadingMatchesToday()
                }
            )
        }
        item {
            Text(
                text = stringResource(SharedRes.strings.events),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        when (val state = events) {
            is EventsState.Loading -> {
                repeat(5) {
                    item {
                        Box(
                            modifier = Modifier.fillParentMaxWidth().height(100.dp).shimmer(CardDefaults.shape)
                        )
                    }
                }
            }
            is EventsState.Error -> {
                item {
                    ErrorContent(
                        text = SharedRes.strings.events_error,
                        modifier = Modifier.fillParentMaxWidth(),
                        retry = {
                            component.retryLoadingEventsToday()
                        }
                    )
                }
            }
            is EventsState.Success -> {
                items(
                    state.events.sortedWith(compareBy<Event> { it.startDate }.thenBy { it.endDate })
                ) { event ->
                    EventCard(
                        event = event,
                        modifier = Modifier.fillParentMaxWidth(),
                        onClick = {
                            component.showEventDetails(it)
                        }
                    )
                }
            }
        }
        item {
            Text(
                text = stringResource(SharedRes.strings.upcoming),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            EventOverview(
                eventsState = component.eventsUpcoming,
                modifier = Modifier.fillParentMaxWidth(),
                retry = {
                    component.retryLoadingEventsUpcoming()
                },
                onEventClick = {
                    component.showEventDetails(it)
                }
            )
        }
    }
}