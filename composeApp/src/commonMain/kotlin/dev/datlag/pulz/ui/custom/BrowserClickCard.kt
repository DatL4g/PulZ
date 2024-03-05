package dev.datlag.pulz.ui.custom

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

@Composable
fun BrowserClickCard(
    uri: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
) {
    var launchUri by remember(uri) { mutableStateOf(false) }
    ComposeClickHelper(uri, launchUri)

    Card(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        content = content,
        onClick = {
            launchUri = true
        },
        enabled = enabled,
        interactionSource = interactionSource
    )

    DisposableEffect(uri) {
        onDispose {
            launchUri = false
        }
    }
}

@Composable
fun BrowserClickElevatedCard(
    uri: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = CardDefaults.elevatedShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
) {
    var launchUri by remember(uri) { mutableStateOf(false) }
    ComposeClickHelper(uri, launchUri)

    ElevatedCard(
        modifier = modifier,
        shape = shape,
        colors = colors,
        elevation = elevation,
        content = content,
        onClick = {
            launchUri = true
        },
        enabled = enabled,
        interactionSource = interactionSource
    )

    DisposableEffect(uri) {
        onDispose {
            launchUri = false
        }
    }
}

@Composable
internal expect fun ComposeClickHelper(uri: String?, clicked: Boolean)