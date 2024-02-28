package dev.datlag.gamechanger.ui.navigation.screen.initial.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.Libs
import dev.chrisbanes.haze.haze
import dev.datlag.gamechanger.LocalHaze
import dev.datlag.gamechanger.LocalPaddingValues
import dev.datlag.gamechanger.SharedRes
import dev.datlag.gamechanger.common.plus
import dev.datlag.gamechanger.other.LocalConsentInfo
import dev.datlag.gamechanger.ui.custom.BrowserClickTextButton
import dev.datlag.tooling.Platform
import dev.datlag.tooling.safeSubList
import dev.icerock.moko.resources.compose.readTextAsState
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserScreen(component: UserComponent) {
    val consentInfo = LocalConsentInfo.current
    val padding = PaddingValues(16.dp)

    LazyColumn(
        modifier = Modifier.safeDrawingPadding().fillMaxSize().haze(state = LocalHaze.current),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding,
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
                modifier = Modifier.padding(top = 16.dp),
                text = stringResource(SharedRes.strings.libraries),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        item {
            val libsJson by SharedRes.assets.aboutlibraries.readTextAsState()
            val libs = libsJson?.let { json ->
                Libs.Builder().withJson(json).build()
            }
            Column(
                modifier = Modifier.fillParentMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                libs?.libraries?.safeSubList(0, 5)?.forEach { lib ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.weight(1F),
                                    text = lib.name,
                                    softWrap = true,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2,
                                    fontWeight = FontWeight.Medium
                                )
                                lib.artifactVersion?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                            Text(
                                text = lib.organization?.name?.ifBlank { null } ?: lib.developers.firstOrNull()?.name ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                            FlowRow(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                            ) {
                                lib.licenses.forEach { lic ->
                                    Badge {
                                        Text(text = lic.name)
                                    }
                                }
                            }
                        }
                    }
                }
                TextButton(
                    modifier = Modifier.fillParentMaxWidth(),
                    onClick = {

                    }
                ) {
                    Text(
                        text = "More"
                    )
                }
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