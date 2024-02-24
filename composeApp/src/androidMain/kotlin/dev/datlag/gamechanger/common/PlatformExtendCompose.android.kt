package dev.datlag.gamechanger.common

import android.content.Intent
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import dev.datlag.tooling.compose.onClick
import dev.datlag.tooling.scopeCatching

actual fun Modifier.browserClick(uri: String?): Modifier = composed {
    if (uri != null) {
        val context = LocalContext.current

        this.onClick {
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
    } else {
        this
    }
}