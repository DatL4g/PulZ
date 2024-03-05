package dev.datlag.pulz.ui.navigation.screen.initial.discover.details.dialog.platform.requirements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.common.mapToIcon
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun PlatformRequirementsDialog(component: PlatformRequirementsComponent) {
    AlertDialog(
        onDismissRequest = {
            component.dismiss()
        },
        icon = component.platformInfo.platform?.mapToIcon()?.let { {
            Image(
                painter = painterResource(it),
                contentDescription = component.platformInfo.platform?.name,
                modifier = Modifier.size(48.dp),
                colorFilter = ColorFilter.tint(LocalContentColor.current)
            )
        } },
        title = {
            Text(
                text = stringResource(SharedRes.strings.requirements),
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                component.platformInfo.requirements?.minimum?.let { min ->
                    Text(
                        text = stringResource(SharedRes.strings.minimum),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = min)
                }
                component.platformInfo.requirements?.recommended?.let { rec ->
                    Text(
                        text = stringResource(SharedRes.strings.recommended),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = rec)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    component.dismiss()
                }
            ) {
                Text(text = stringResource(SharedRes.strings.close))
            }
        }
    )
}