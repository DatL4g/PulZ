package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.octane.model.Event
import dev.datlag.gamechanger.octane.state.EventsState
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.component.EventCard
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

@Composable
fun RocketLeagueScreen(component: RocketLeagueComponent) {
    val eventsToday by component.eventsToday.collectAsStateWithLifecycle()

    when (val state = eventsToday) {
        is EventsState.Loading -> {
            Text(text = "Loading")
        }
        is EventsState.Error -> {
            Text(text = "Error")
        }
        is EventsState.Success -> {
            val padding = PaddingValues(16.dp)

            LazyColumn(
                modifier = Modifier.fillMaxSize().haze(LocalHaze.current),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = component.game.heroUrl,
                            contentDescription = stringResource(SharedRes.strings.rocket_league),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                item {
                    Text(
                        text = stringResource(SharedRes.strings.happening_now),
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillParentMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.events.sortedWith(compareBy<Event> { it.startDate }.thenBy { it.endDate })) { event ->
                            EventCard(event)
                        }
                    }
                }
            }
        }
    }
}