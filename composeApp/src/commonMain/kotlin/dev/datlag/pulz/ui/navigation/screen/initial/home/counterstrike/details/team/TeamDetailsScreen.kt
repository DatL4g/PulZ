package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.chrisbanes.haze.haze
import dev.datlag.pulz.LocalHaze
import dev.datlag.pulz.LocalPaddingValues
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.common.browserClick
import dev.datlag.pulz.common.image
import dev.datlag.pulz.common.plus
import dev.datlag.pulz.common.shimmer
import dev.datlag.pulz.game.DEFAULT
import dev.datlag.pulz.game.HOME
import dev.datlag.pulz.hltv.model.Country
import dev.datlag.pulz.hltv.model.Team
import dev.datlag.pulz.hltv.state.TeamStateMachine
import dev.datlag.pulz.other.CountryImage
import dev.datlag.pulz.ui.navigation.screen.initial.component.ErrorContent
import dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.details.team.component.PlayerCard
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.JsonNull
import okio.FileSystem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeamDetailsScreen(component: TeamDetailsComponent) {
    val padding = PaddingValues(16.dp)
    val teamState by component.teamState.collectAsStateWithLifecycle()

    when (val state = teamState) {
        is TeamStateMachine.State.Loading -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TeamHeader(
                        image = component.initialTeam.image(),
                        country = null,
                        name = component.initialTeam.name,
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
                repeat(5) {
                    item {
                        Box(modifier = Modifier.fillParentMaxWidth().height(100.dp).shimmer(CardDefaults.shape))
                    }
                }
            }
        }
        is TeamStateMachine.State.Error -> {
            ErrorContent(
                text = SharedRes.strings.team_details_error,
                modifier = Modifier.fillMaxSize().padding(
                    LocalPaddingValues.current?.plus(padding) ?: padding
                ),
                retry = {
                    component.retryLoading()
                }
            )
        }
        is TeamStateMachine.State.Success -> {
            SideEffect {
                Napier.e(state.team.toString())
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize().haze(LocalHaze.current),
                contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    TeamHeader(
                        image = state.team.image(),
                        country = state.team.country,
                        name = state.team.name,
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
                if (!state.team.social.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = stringResource(SharedRes.strings.social),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier.fillParentMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (!state.team.social.facebook.isNullOrBlank()) {
                                    Text(
                                        text = stringResource(SharedRes.strings.facebook_colon),
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1
                                    )
                                }
                                if (!state.team.social.twitter.isNullOrBlank()) {
                                    Text(
                                        text = stringResource(SharedRes.strings.twitter_colon),
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1
                                    )
                                }
                                if (!state.team.social.instagram.isNullOrBlank()) {
                                    Text(
                                        text = stringResource(SharedRes.strings.instagram_colon),
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 1
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier.weight(1F),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (!state.team.social.facebook.isNullOrBlank()) {
                                    Text(
                                        text = state.team.social.facebookName ?: state.team.social.facebook!!,
                                        maxLines = 1,
                                        modifier = Modifier.browserClick(state.team.social.facebook!!)
                                    )
                                }
                                if (!state.team.social.twitter.isNullOrBlank()) {
                                    Text(
                                        text = state.team.social.twitterName ?: state.team.social.twitter!!,
                                        maxLines = 1,
                                        modifier = Modifier.browserClick(state.team.social.twitter!!)
                                    )
                                }
                                if (!state.team.social.instagram.isNullOrBlank()) {
                                    Text(
                                        text = state.team.social.instaName ?: state.team.social.instagram!!,
                                        maxLines = 1,
                                        modifier = Modifier.browserClick(state.team.social.instagram!!)
                                    )
                                }
                            }
                        }
                    }
                }
                if (state.team.players.any { it.type.isActive }) {
                    item {
                        Text(
                            modifier = Modifier.padding(top = 16.dp),
                            text = stringResource(SharedRes.strings.players),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    item {
                        FlowRow(
                            modifier = Modifier.fillParentMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            state.team.players.filter { it.type.isActive }.forEach {
                                PlayerCard(it, Modifier.size(100.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TeamHeader(image: String?, name: String, country: Country?, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            modifier = Modifier.size(100.dp),
            model = image,
            contentDescription = name
        )
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            country?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val res = remember(it) { CountryImage.getByCode(it.code) }

                    Image(
                        painter = painterResource(res),
                        contentDescription = it.name,
                        modifier = Modifier
                            .size(16.dp)
                            .clip(MaterialTheme.shapes.extraSmall)
                            .border(1.dp, LocalContentColor.current, MaterialTheme.shapes.extraSmall)
                    )
                    Text(text = it.name)
                }
            }
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}