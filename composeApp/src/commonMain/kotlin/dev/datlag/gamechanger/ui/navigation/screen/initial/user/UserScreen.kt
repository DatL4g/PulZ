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
import dev.datlag.gamechanger.ui.custom.BrowserClickTextButton
import dev.datlag.gamechanger.ui.navigation.screen.initial.user.component.SteamContent
import dev.datlag.gamechanger.ui.theme.SchemeTheme
import dev.datlag.tooling.Platform
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserScreen(component: UserComponent) {
    val consentInfo = LocalConsentInfo.current

    LazyColumn(
        modifier = Modifier.safeDrawingPadding().fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(SharedRes.strings.settings),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item {
            Text(
                text = stringResource(SharedRes.strings.account),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        if (!component.isSignedIn) {
            item {
                TextButton(
                    onClick = {
                        component.login()
                    }
                ) {
                    Text(text = stringResource(SharedRes.strings.login))
                }
            }
        } else {
            item {
                val infoText = if (Platform.isDesktop) {
                    SharedRes.strings.steam_accounts_synced
                } else {
                    SharedRes.strings.connecting_steam_requires_desktop
                }

                Text(text = stringResource(infoText))
            }
        }
        item {
            Text(
                text = stringResource(SharedRes.strings.privacy),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            FlowRow(
                modifier = Modifier.fillParentMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
            ) {
                if (consentInfo.privacy) {
                    TextButton(
                        onClick = {
                            consentInfo.showPrivacyForm()
                        }
                    ) {
                        Text(text = stringResource(SharedRes.strings.edit_consent))
                    }
                }
                BrowserClickTextButton(
                    uri = "https://github.com/DatL4g/Gamechanger/blob/master/Privacy_Policy.md"
                ) {
                    Text(text = stringResource(SharedRes.strings.policy))
                }
            }
        }
    }
}