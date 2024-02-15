package dev.datlag.gamechanger.ui.navigation.screen.initial.user.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.datlag.gamechanger.game.SteamLauncher
import dev.datlag.gamechanger.game.model.steam.User
import dev.datlag.gamechanger.ui.navigation.screen.initial.user.UserComponent
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalLayoutApi::class)
fun LazyGridScope.SteamContent(component: UserComponent) {
    SteamLauncher.loggedInUsers.forEach {
        item {
            SchemeTheme(
                key = it.id
            ) {
                SteamUserCard(user = it)
            }
        }
    }
}

@Composable
private fun SteamUserCard(user: User, onClick: () -> Unit = { }) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val scope = rememberCoroutineScope()

                AsyncImage(
                    model = user.avatarPath,
                    contentDescription = user.name,
                    modifier = Modifier.size(56.dp).clip(CircleShape),
                    onSuccess = { state ->
                        SchemeTheme.update(
                            key = user.id,
                            input = state.painter,
                            scope = scope
                        )
                    },
                    clipToBounds = true
                )
                Text(
                    text = user.name,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
            Box(
                modifier = Modifier.padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(DividerDefaults.Thickness)
                    .background(DividerDefaults.color)
            )
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "ID:",
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Account:",
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    SelectionContainer {
                        Text(text = user.id)
                    }
                    SelectionContainer {
                        Text(text = user.config.accountName)
                    }
                }
            }
        }
    }
}