package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component.hltv.EventCover
import dev.icerock.moko.resources.compose.stringResource

fun LazyGridScope.HLTVContent(homeState: HomeStateMachine.State) {
    when (homeState) {
        is HomeStateMachine.State.Loading -> {
            item {
                Text(text = "Loading home info")
            }
        }
        is HomeStateMachine.State.Error -> {
            item {
                Text(text = "Error loading home")
            }
        }
        is HomeStateMachine.State.Success -> {
            homeState.home.event?.let {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(SharedRes.strings.event),
                            style = MaterialTheme.typography.titleLarge
                        )
                        EventCover(it) {
                            println("Load event")
                        }
                    }
                }
            }
            homeState.home.hero?.let {
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(SharedRes.strings.trending),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {

                            }
                        ) {
                            AsyncImage(
                                model = it.image,
                                contentDescription = stringResource(SharedRes.strings.trending),
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.FillWidth,
                            )
                        }
                    }
                }
            }
        }
    }
}