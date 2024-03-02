package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.shimmer
import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.ui.navigation.screen.initial.component.ErrorContent
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun EventOverview(
    eventsState: StateFlow<EventsState>,
    modifier: Modifier = Modifier,
    retry: () -> Unit
) {
    val matches by eventsState.collectAsStateWithLifecycle()

    when (val state = matches) {
        is EventsState.Loading -> {
            LazyRow(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(5) {
                    item {
                        Box(
                            modifier = Modifier.width(200.dp).height(100.dp).shimmer(CardDefaults.shape)
                        )
                    }
                }
            }
        }
        is EventsState.Error -> {
            ErrorContent(
                text = SharedRes.strings.events_error,
                modifier = modifier,
                retry = retry
            )
        }
        is EventsState.Success -> {
            LazyRow(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.events.sortedWith(compareBy<Event> { it.startDate }.thenBy { it.endDate })
                ) { event ->
                    EventCard(
                        event = event,
                        modifier = Modifier.width(200.dp).fillParentMaxHeight()
                    )
                }
            }
        }
    }
}