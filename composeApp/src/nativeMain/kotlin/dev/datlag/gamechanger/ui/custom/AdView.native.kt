package dev.datlag.gamechanger.ui.custom

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

actual fun LazyListScope.AdView(id: String, type: AdType) {
}

actual object Ads {
    actual fun native(): String? {
        return null
    }
    actual fun banner(): String? {
        return null
    }
}

@Composable
actual fun BannerAd(id: String, modifier: Modifier) {
}