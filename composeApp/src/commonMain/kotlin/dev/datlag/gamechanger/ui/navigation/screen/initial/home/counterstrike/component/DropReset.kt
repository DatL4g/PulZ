package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike.CounterStrikeComponent
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.DateTimePeriod

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DropReset(periodState: StateFlow<DateTimePeriod>, modifier: Modifier = Modifier) {
    val period by periodState.collectAsStateWithLifecycle()

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(SharedRes.strings.drop_reset),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                Text(text = stringResource(SharedRes.strings.placeholder_days, period.days))
                Text(text = stringResource(SharedRes.strings.placeholder_hours, period.hours))
                Text(text = stringResource(SharedRes.strings.placeholder_minutes, period.minutes))
                Text(text = stringResource(SharedRes.strings.placeholder_seconds, period.seconds))
            }
        }
    }
}