package dev.datlag.pulz.ui.navigation.screen.initial.home.counterstrike.article

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.datlag.pulz.LocalPaddingValues
import dev.datlag.pulz.ui.custom.BrowserView
import io.github.aakira.napier.Napier

@Composable
fun ArticleScreen(component: ArticleComponent) {
    LaunchedEffect(component.link) {
        Napier.e("Browser URL: ${component.link}")
    }

    BrowserView(
        url = component.link,
        modifier = Modifier.fillMaxSize().padding(LocalPaddingValues.current ?: PaddingValues(0.dp))
    )
}