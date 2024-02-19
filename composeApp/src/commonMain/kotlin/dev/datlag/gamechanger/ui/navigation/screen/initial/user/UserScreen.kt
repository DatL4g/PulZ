package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.fullRow
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.ui.navigation.screen.initial.user.component.SteamContent
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun UserScreen(component: UserComponent) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        fullRow {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(SharedRes.strings.settings)
                    )
                }
            }
        }
        fullRow {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                val scope = rememberCoroutineScope()

                AsyncImage(
                    model = SteamLauncher.loggedInUsers.firstOrNull()?.avatarPath,
                    contentDescription = SteamLauncher.loggedInUsers.firstOrNull()?.name,
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    clipToBounds = true,
                    onSuccess = { state ->
                        SchemeTheme.update(
                            key = SteamLauncher.loggedInUsers.firstOrNull()?.id,
                            input = state.painter,
                            scope = scope
                        )
                    }
                )
            }
        }
        fullRow {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = SteamLauncher.loggedInUsers.firstOrNull()?.name ?: stringResource(SharedRes.strings.user),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    maxLines = 2,
                    textAlign = TextAlign.Center
                )
            }
        }
        fullRow {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                SelectionContainer {
                    Text(text = SteamLauncher.loggedInUsers.firstOrNull()?.id ?: "")
                }
            }
        }
        fullRow {
            Text(
                text = stringResource(SharedRes.strings.steam),
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge
            )
        }
        SteamContent(component)
    }
}