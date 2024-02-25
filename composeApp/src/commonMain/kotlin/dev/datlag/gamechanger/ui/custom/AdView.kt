package dev.datlag.gamechanger.ui.custom

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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

@Composable
expect fun BannerAd(
    id: String,
    modifier: Modifier = Modifier
)

@Composable
fun BannerAd(modifier: Modifier = Modifier) {
    Ads.banner()?.let {
        BannerAd(
            id = it,
            modifier = modifier
        )
    }
}

sealed interface AdType {
    data object Native : AdType
    data object Banner : AdType
}

expect object Ads {
    fun native(): String?
    fun banner(): String?
}