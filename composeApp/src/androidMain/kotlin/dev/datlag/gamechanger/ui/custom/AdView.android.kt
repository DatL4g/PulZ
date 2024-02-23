package dev.datlag.gamechanger.ui.custom

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAdOptions
import dev.datlag.gamechanger.Sekret
import dev.datlag.gamechanger.getPackageName
import dev.datlag.gamechanger.other.StateSaver
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
}