package dev.datlag.gamechanger.ui.navigation.screen.initial.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import dev.datlag.gamechanger.ui.theme.rememberSchemeThemeDominantColorState
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource
import io.github.aakira.napier.Napier

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
    val csGame = Game.Steam.CounterStrike
    val csColor = rememberSchemeThemeDominantColor(csGame) ?: Color.Black
    val rlGame = Game.Steam.RocketLeague
    val rlColor = rememberSchemeThemeDominantColor(rlGame) ?: Color.Black

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().haze(state = LocalHaze.current),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding,
        columns = GridCells.Adaptive(512.dp)
    ) {
        item(
            key = listOf(csGame.id, csColor.toArgb())
        ) {
            GameCover(
                title = stringResource(SharedRes.strings.counter_strike),
                game = csGame,
                fallback = SharedRes.images.cs_banner,
                color = csColor,
                modifier = Modifier.fillMaxWidth().clip(CardDefaults.shape)
            ) {
                component.showCounterStrike()
            }
        }
        item(
            key = listOf(rlGame.id, rlColor.toArgb())
        ) {
            GameCover(
                title = stringResource(SharedRes.strings.rocket_league),
                game = rlGame,
                fallback = SharedRes.images.rl_banner,
                color = rlColor,
                modifier = Modifier.fillMaxWidth().clip(CardDefaults.shape)
            )
        }
    }
}