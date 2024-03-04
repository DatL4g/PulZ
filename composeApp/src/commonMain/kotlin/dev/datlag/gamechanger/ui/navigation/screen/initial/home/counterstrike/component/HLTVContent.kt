package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.shimmer
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.ui.navigation.screen.initial.component.ErrorContent
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component.hltv.EventCover
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component.hltv.NewsCard
import dev.icerock.moko.resources.compose.stringResource

fun LazyListScope.HLTVContent(
    homeState: HomeStateMachine.State,
    retry: () -> Unit
) {
    when (homeState) {
        is HomeStateMachine.State.Loading -> {
            repeat(5) {
                item {
                    Box(modifier = Modifier.fillParentMaxWidth().height(100.dp).shimmer(CardDefaults.shape))
                }
            }
        }
        is HomeStateMachine.State.Error -> {
            item {
                ErrorContent(
                    text = SharedRes.strings.hltv_error,
                    modifier = Modifier.fillParentMaxWidth(),
                    retry = retry
                )
            }
        }
        is HomeStateMachine.State.Success -> {
            homeState.home.event?.let {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxWidth(),
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
                        modifier = Modifier.fillParentMaxWidth(),
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
            if (homeState.home.news.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(SharedRes.strings.news),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                items(homeState.home.news) { news ->
                    NewsCard(news, Modifier.fillMaxWidth())
                }
            }
        }
    }
}