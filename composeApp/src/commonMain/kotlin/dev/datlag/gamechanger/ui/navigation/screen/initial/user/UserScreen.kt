package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
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
import dev.datlag.gamechanger.other.LocalConsentInfo
import dev.datlag.gamechanger.ui.navigation.screen.initial.user.component.SteamContent
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun UserScreen(component: UserComponent) {
    val consentInfo = LocalConsentInfo.current

    LazyColumn(
        modifier = Modifier.fillMaxSize().safeDrawingPadding(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (consentInfo.privacy) {
            item {
                Button(
                    onClick = {
                        consentInfo.showPrivacyForm()
                    }
                ) {
                    Text(text = "Privacy")
                }
            }
        }
    }
}