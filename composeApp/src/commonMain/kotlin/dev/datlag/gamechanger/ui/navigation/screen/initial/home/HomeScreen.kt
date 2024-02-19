package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.game.Game
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.component.GameCover
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColor
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle

@Composable
fun HomeScreen(component: HomeComponent) {
    val showWelcome by component.showWelcome.collectAsStateWithLifecycle(true)
    val childState by component.child.subscribeAsState()

    childState.child?.instance?.render() ?: Overview(component)

    /*Column {
        if (showWelcome) {
            Text(text = "Welcome screen should be shown")
        } else {
            Text(text = "Home screen visible")
        }
        Button(
            onClick = {
                component.setWelcome(showWelcome)
            }
        ) {
            Icon(
                imageVector = Icons.Default.AdsClick,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = "Click to trigger welcome screen")
        }
    }*/
}

@Composable
private fun Overview(component: HomeComponent) {
    val padding = PaddingValues(all = 16.dp)

    LazyColumn(
        modifier = Modifier.fillMaxSize().haze(state = LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding
    ) {
        repeat(5) {
            item {
                val game = Game.Steam.CounterStrike

                GameCover(
                    title = "Counter Strike",
                    game = game,
                    fallback = SharedRes.images.cs_banner,
                    color = rememberSchemeThemeDominantColor(game) ?: MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(512.dp).clip(RoundedCornerShape(24.dp))
                ) {
                    component.showCounterStrike()
                }
            }
        }
    }
}