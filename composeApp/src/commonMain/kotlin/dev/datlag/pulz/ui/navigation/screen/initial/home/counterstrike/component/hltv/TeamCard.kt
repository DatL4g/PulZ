package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.component.hltv

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.pulz.common.bottomShadowBrush
import dev.datlag.pulz.common.image
import dev.datlag.pulz.hltv.model.Home
import dev.datlag.pulz.ui.theme.SchemeTheme
import dev.datlag.pulz.ui.theme.rememberSchemeThemeDominantColorState

@Composable
fun TeamCard(
    team: Home.Team,
    onClick: (Home.Team) -> Unit
) {
    SchemeTheme(
        key = team.href
    ) {
        Card(
            modifier = Modifier.size(150.dp),
            onClick = {
                onClick(team)
            }
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                val scope = rememberCoroutineScope()
                val colorState = rememberSchemeThemeDominantColorState(
                    key = team.href,
                    applyMinContrast = true,
                    minContrastBackgroundColor = MaterialTheme.colorScheme.surfaceVariant
                )
                val animatedColor by animateColorAsState(
                    targetValue = colorState.color
                )

                AsyncImage(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    model = team.image(),
                    contentDescription = team.name,
                    onSuccess = { state ->
                        SchemeTheme.update(
                            key = team.href,
                            input = state.painter,
                            scope = scope
                        )
                    }
                )

                Column(
                    modifier = Modifier
                        .matchParentSize()
                        .bottomShadowBrush(animatedColor, 0.25F)
                        .padding(8.dp)
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = buildString {
                            append(team.rank)
                            append(" ")
                            append(team.name)
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}