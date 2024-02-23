package dev.datlag.gamechanger.ui.custom

import androidx.compose.foundation.lazy.LazyListScope

expect fun LazyListScope.AdView(
    id: String,
    type: AdType
)

fun LazyListScope.NativeAdView() {
    Ads.native()?.let {
        AdView(
            id = it,
            type = AdType.Native
        )
    }
}

sealed interface AdType {
    data object Native : AdType
}

expect object Ads {
    fun native(): String?
}