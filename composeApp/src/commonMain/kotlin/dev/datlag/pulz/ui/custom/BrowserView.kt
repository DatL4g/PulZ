package dev.datlag.pulz.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BrowserView(url: String, modifier: Modifier = Modifier)