package dev.datlag.gamechanger.ui.navigation.screen.initial.home.counterstrike

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.hltv.state.HomeStateMachine
import dev.datlag.gamechanger.ui.custom.RoundTab
import dev.datlag.gamechanger.ui.navigation.screen.initial.home.component.HLTVContent
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun CounterStrikeScreen(component: CounterStrikeComponent) {
    var selectedTab by remember { mutableIntStateOf(
        if (component.canLaunch && SteamLauncher.loggedInUsers.isNotEmpty()) {
            0
        } else {
            1
        }
    ) }
    val htlvHome by component.hltvHomeState.collectAsStateWithLifecycle(initialValue = HomeStateMachine.State.Loading)

    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillParentMaxWidth(),
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxWidth(),
                    model = component.game.heroUrl,
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth
                )
            }
        }
        item {
            TabRow(
                modifier = Modifier.fillParentMaxWidth(),
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                indicator = { },
                divider = { }
            ) {
                RoundTab(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(SharedRes.strings.game)
                    )
                }
                RoundTab(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                    }
                ) {
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(SharedRes.strings.info)
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // ToDo("game content")
            }
            1 -> {
                HLTVContent(homeState = htlvHome)
            }
        }
    }


}