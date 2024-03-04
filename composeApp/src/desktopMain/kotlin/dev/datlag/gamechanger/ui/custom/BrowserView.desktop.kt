package dev.datlag.gamechanger.ui.custom

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.github.winterreisender.webviewko.WebviewKoAWT

@Composable
actual fun BrowserView(url: String, modifier: Modifier) {
    Webview(
        url = url,
        modifier = modifier
    )
}

@Composable
private fun Webview(
    url: String,
    modifier: Modifier = Modifier
) {
    val webview = remember {
        WebviewKoAWT(0) {
            it.navigate(url)
            it.show()
        }
    }

    LaunchedEffect(url) {
        if(webview.isInitialized)
            webview.dispatch {
                navigate(url)
            }
    }

    SwingPanel(
        modifier= modifier,
        factory = {
            webview
        }
    )
}
