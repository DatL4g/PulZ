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
import dev.datlag.gamechanger.octane.state.MatchesState
import dev.datlag.gamechanger.ui.navigation.screen.initial.component.ErrorContent
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun MatchOverview(
    matchesState: StateFlow<MatchesState>,
    modifier: Modifier = Modifier,
    retry: () -> Unit
) {
    val matches by matchesState.collectAsStateWithLifecycle()

    when (val state = matches) {
        is MatchesState.Loading -> {
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
        is MatchesState.Error -> {
            ErrorContent(
                text = SharedRes.strings.matches_error,
                modifier = modifier,
                retry = retry
            )
        }
        is MatchesState.Success -> {
            LazyRow(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.matches.sortedWith(compareBy { it.date })
                ) { match ->
                    MatchCard(match)
                }
            }
        }
    }
}
