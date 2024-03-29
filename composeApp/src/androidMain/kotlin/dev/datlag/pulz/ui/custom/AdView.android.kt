package dev.datlag.pulz.ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAdOptions
import dev.datlag.pulz.Sekret
import dev.datlag.pulz.getPackageName
import dev.datlag.pulz.other.StateSaver
import dev.datlag.tooling.decompose.lifecycle.collectAsStateWithLifecycle
import io.github.aakira.napier.Napier

actual fun LazyListScope.AdView(id: String, type: AdType) {
    item(key = id) {
        val nativeAdState = rememberCustomNativeAdState(
            adUnit = id,
            nativeAdOptions = NativeAdOptions.Builder()
                .setVideoOptions(
                    VideoOptions.Builder()
                        .setStartMuted(true).setClickToExpandRequested(true)
                        .build()
                ).setRequestMultipleImages(true)
                .build(),
            adListener = object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    Napier.e(p0.message)
                }
            }
        )

        val nativeAd by nativeAdState.nativeAd.collectAsStateWithLifecycle()

        nativeAd?.let { NativeAdCard(it, Modifier.fillParentMaxWidth()) }
    }
}

actual object Ads {
    actual fun native(): String? {
        return if (StateSaver.sekretLibraryLoaded) {
            Sekret.androidAdNative(getPackageName())
        } else {
            null
        }
    }

    actual fun banner(): String? {
        return if (StateSaver.sekretLibraryLoaded) {
            Sekret.androidAdBanner(getPackageName())
        } else {
            null
        }
    }
}

@Composable
actual fun BannerAd(id: String, modifier: Modifier) {
    AndroidView(
        factory = { context ->
            AdView(context).apply {
                adUnitId = id
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
            }
        },
        modifier = modifier
    )
}