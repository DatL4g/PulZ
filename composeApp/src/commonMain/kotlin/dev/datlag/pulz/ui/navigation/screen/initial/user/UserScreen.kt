package dev.datlag.pulz.ui.navigation.screen.initial.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.entity.Library
import dev.chrisbanes.haze.haze
import dev.datlag.pulz.LocalHaze
import dev.datlag.pulz.LocalPaddingValues
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.common.plus
import dev.datlag.pulz.other.Constants
import dev.datlag.pulz.other.LocalConsentInfo
import dev.datlag.pulz.ui.custom.BrowserClickTextButton
import dev.datlag.pulz.ui.navigation.screen.initial.user.component.LibraryCard
import dev.datlag.tooling.Platform
import dev.datlag.tooling.safeSubList
import dev.icerock.moko.resources.compose.readTextAsState
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UserScreen(component: UserComponent) {
    val consentInfo = LocalConsentInfo.current
    val padding = PaddingValues(16.dp)
    val libsJson by SharedRes.assets.aboutlibraries.readTextAsState()
    val libs = remember(libsJson) {
        libsJson?.let { json ->
            Libs.Builder().withJson(json).build()
        }
    }
    val libsSize = remember(libs) {
        libs?.libraries?.size
    }
    var libsShowing by remember {
        mutableIntStateOf(5)
    }
    val canShowMore = remember(libsSize, libsShowing) {
        (libsSize ?: 0) > libsShowing
    }
    val libsSubList = remember(libs, libsShowing) {
        libs?.libraries?.safeSubList(0, libsShowing) ?: emptyList()
    }
    val dialogState by component.dialog.subscribeAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().haze(state = LocalHaze.current),
        contentPadding = LocalPaddingValues.current?.plus(padding) ?: padding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                modifier = Modifier.padding(vertical = 16.dp),
                text = stringResource(SharedRes.strings.settings),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineLarge
            )
        }
        if (!component.isInstantApp) {
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
                    Column(
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        Text(text = stringResource(SharedRes.strings.accounts_advantage))
                        Text(text = stringResource(SharedRes.strings.requires_desktop))
                    }
                }
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
        if (libsSize == null || libsSize <= 0) {
            item {
                Text(text = stringResource(SharedRes.strings.libraries_not_loaded))
            }
        } else {
            items(libsSubList) { lib ->
                LibraryCard(
                    library = lib,
                    onLicenseClick = component::licenseDetails,
                    onClick = component::libraryDetails
                )
            }
            if (canShowMore) {
                item {
                    TextButton(
                        modifier = Modifier.fillParentMaxWidth(),
                        onClick = {
                            libsShowing += 5
                        }
                    ) {
                        Text(text = stringResource(SharedRes.strings.more))
                    }
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
                    uri = Constants.PRIVACY_POLICY
                ) {
                    Text(text = stringResource(SharedRes.strings.policy))
                }
                BrowserClickTextButton(
                    uri = Constants.TERMS_CONDITIONS
                ) {
                    Text(text = stringResource(SharedRes.strings.terms_conditions))
                }
                BrowserClickTextButton(
                    uri = Constants.LICENSE
                ) {
                    Text(text = stringResource(SharedRes.strings.license))
                }
            }
        }
    }

    dialogState.child?.instance?.render()
}

