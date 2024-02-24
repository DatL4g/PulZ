package dev.datlag.gamechanger.ui.custom

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import dev.datlag.tooling.scopeCatching

@Composable
internal actual fun BrowserClickCardHelper(uri: String?, clicked: Boolean) {
    val context = LocalContext.current

    LaunchedEffect(clicked) {
        if (clicked && uri != null) {
            val browserIntent = Intent(Intent.ACTION_VIEW, uri.toUri())

            val done = scopeCatching {
                ContextCompat.startActivity(context, browserIntent, null)
            }.isSuccess

            if (!done) {
                val newIntent = browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                scopeCatching {
                    ContextCompat.startActivity(context, newIntent, null)
                }
            }
        }
    }
}