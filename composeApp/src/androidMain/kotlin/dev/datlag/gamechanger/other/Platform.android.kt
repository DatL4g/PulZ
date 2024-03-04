package dev.datlag.gamechanger.other

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.instantapps.InstantApps
import dev.datlag.gamechanger.getPackageName

actual class Platform(private val context: Context) {
    actual fun isInstantApp(): Boolean {
        return InstantApps.getPackageManagerCompat(context).isInstantApp
    }

    actual companion object {
        @Composable
        actual fun requestInstantAppInstall() {
            val activityContext = LocalContext.current

            SideEffect {
                activityContext.findActivity()?.let {
                    InstantApps.showInstallPrompt(it, null, 1337, null)
                }
            }
        }

        private tailrec fun Context.findActivity(): Activity? = when (this) {
            is Activity -> this
            is ContextWrapper -> baseContext.findActivity()
            else -> null
        }
    }
}