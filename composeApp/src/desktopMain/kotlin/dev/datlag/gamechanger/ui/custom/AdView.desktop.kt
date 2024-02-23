package dev.datlag.gamechanger.ui.custom

import androidx.compose.foundation.lazy.LazyListScope

actual fun LazyListScope.AdView(id: String, type: AdType) {
}

actual object Ads {
    actual fun native(): String? {
        return null
    }
}