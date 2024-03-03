package dev.datlag.gamechanger.ui.navigation.screen.initial.home.rocketleague.details.match.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.octane.model.Match
import dev.datlag.gamechanger.ui.custom.seeker.Seeker
import dev.datlag.gamechanger.ui.custom.seeker.SeekerDefaults
import dev.datlag.gamechanger.ui.custom.seeker.Segment
import dev.icerock.moko.resources.compose.stringResource
import kotlin.math.min

@Composable
fun StatisticSeeker(
    blueValue: Float?,
    orangeValue: Float?,
    blueName: String?,
    orangeName: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val maxValue = remember(blueValue, orangeValue) {
            (blueValue ?: 0F) + (orangeValue ?: 0F)
        }

        Seeker(
            modifier = Modifier.fillMaxWidth(),
            range = 0F..maxValue,
            segments = listOf(
                Segment(
                    name = blueName ?: stringResource(SharedRes.strings.blue),
                    start = 0F
                ),
                Segment(
                    name = orangeName ?: stringResource(SharedRes.strings.blue),
                    start = min((blueValue ?: 0F), maxValue)
                )
            ),
            value = blueValue ?: 0F,
            onValueChange = { },
            dimensions = SeekerDefaults.seekerDimensions(
                thumbRadius = 0.dp,
                gap = 4.dp
            ),
            progressStartPosition = if ((blueValue ?: 0F) < (orangeValue ?: 0F)) {
                maxValue
            } else {
                0F
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = (blueValue?.toInt() ?: 0).toString())
            Text(text = title)
            Text(text = (orangeValue?.toInt() ?: 0).toString())
        }
    }
}

@Composable
fun StatisticSeeker(
    blueValue: Int?,
    orangeValue: Int?,
    blueName: String?,
    orangeName: String?,
    title: String,
    modifier: Modifier = Modifier
) = StatisticSeeker(
    blueValue = blueValue?.toFloat(),
    orangeValue = orangeValue?.toFloat(),
    blueName = blueName,
    orangeName = orangeName,
    title = title,
    modifier = modifier
)

@Composable
fun StatisticSeeker(
    blueValue: Float?,
    orangeValue: Float?,
    match: Match,
    title: String,
    modifier: Modifier = Modifier
) = StatisticSeeker(
    blueValue = blueValue,
    orangeValue = orangeValue,
    blueName = match.blue?.title,
    orangeName = match.orange?.title,
    title = title,
    modifier = modifier
)

@Composable
fun StatisticSeeker(
    blueValue: Int?,
    orangeValue: Int?,
    match: Match,
    title: String,
    modifier: Modifier = Modifier
) = StatisticSeeker(
    blueValue = blueValue,
    orangeValue = orangeValue,
    blueName = match.blue?.title,
    orangeName = match.orange?.title,
    title = title,
    modifier = modifier
)