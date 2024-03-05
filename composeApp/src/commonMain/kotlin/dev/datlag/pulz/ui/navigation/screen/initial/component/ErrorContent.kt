package dev.datlag.pulz.ui.navigation.screen.initial.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.SharedRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ErrorContent(
    text: String,
    modifier: Modifier = Modifier,
    retry: () -> Unit
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text)
        Button(
            onClick = retry
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(SharedRes.strings.try_again),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(text = stringResource(SharedRes.strings.try_again))
        }
    }
}

@Composable
fun ErrorContent(
    text: StringResource,
    modifier: Modifier = Modifier,
    retry: () -> Unit
) = ErrorContent(
    text = stringResource(text),
    modifier = modifier,
    retry = retry
)