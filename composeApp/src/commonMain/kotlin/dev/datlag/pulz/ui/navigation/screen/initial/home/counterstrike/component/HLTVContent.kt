package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.common.shimmer
import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.hltv.state.HomeStateMachine
import dev.datlag.pulz.ui.navigation.screen.initial.component.ErrorContent
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component.hltv.EventCover
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component.hltv.NewsCard
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component.hltv.TeamCard
import dev.icerock.moko.resources.compose.stringResource

fun LazyListScope.HLTVContent(
    homeState: HomeStateMachine.State,
    news: List<Home.News>,
    canShowMoreNews: Boolean,
    onShowMoreNews: () -> Unit,
    retry: () -> Unit,
    articleClick: (String) -> Unit,
    teamClick: (Home.Team) -> Unit
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
                            articleClick(it.href)
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
                                articleClick(it.href)
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
            if (news.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(SharedRes.strings.news),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                items(news) { new ->
                    NewsCard(
                        news = new,
                        modifier = Modifier.fillParentMaxWidth(),
                        onClick = {
                            articleClick(it.link)
                        }
                    )
                }
                if (canShowMoreNews) {
                    item {
                        TextButton(
                            onClick = {
                                onShowMoreNews()
                            },
                            modifier = Modifier.fillParentMaxWidth()
                        ) {
                            Text(text = stringResource(SharedRes.strings.more))
                        }
                    }
                }
            }
            if (homeState.home.teams.isNotEmpty()) {
                item {
                    Text(
                        text = stringResource(SharedRes.strings.teams),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(homeState.home.teams, key = { it.href }) { team ->
                            TeamCard(team) {
                                teamClick(it)
                            }
                        }
                    }
                }
            }
        }
    }
}