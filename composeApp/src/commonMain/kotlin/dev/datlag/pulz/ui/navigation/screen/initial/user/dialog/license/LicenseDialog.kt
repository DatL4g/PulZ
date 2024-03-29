package dev.datlag.pulz.ui.navigation.screen.initial.user.dialog.license

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.SharedRes
import dev.datlag.pulz.ui.custom.BrowserClickFilledTonalButton
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun LicenseDialog(component: LicenseComponent) {
    AlertDialog(
        onDismissRequest = {
            component.dismiss()
        },
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = component.name,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
            ) {
                component.year?.ifBlank { null }?.let {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                            ) {
                                append(stringResource(SharedRes.strings.year_colon))
                            }
                            append(" ")
                            append(it)
                        },
                    )
                }
                Text(text = component.description)
            }
        },
        dismissButton = if (!component.url.isNullOrBlank()) {
            {
                BrowserClickFilledTonalButton(
                    uri = component.url
                ) {
                    Text(text = stringResource(SharedRes.strings.website))
                }
            }
        } else {
            null
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