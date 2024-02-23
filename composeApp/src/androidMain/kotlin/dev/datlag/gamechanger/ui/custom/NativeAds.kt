package dev.datlag.gamechanger.ui.custom

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil3.compose.AsyncImage
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MediaContent
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.roundToInt

@Composable
fun NativeAdViewCompose(
    modifier: Modifier = Modifier,
    content: @Composable (nativeAdView: NativeAdView) -> Unit
) = AndroidView(
    modifier = modifier,
    factory = {
        NativeAdView(it)
    },
    update = {
        val composeView = ComposeView(it.context)
        it.removeAllViews()
        it.addView(composeView)
        composeView.setContent {
            content(it)
        }
    }
)

@Composable
fun NativeAdView(
    modifier: Modifier = Modifier,
    getView: (ComposeView) -> Unit,
    content: @Composable () -> Unit
) = AndroidView(
    modifier = modifier,
    factory = {
        ComposeView(it)
    },
    update = {
        it.setContent {
            content()
        }
        getView(it)
    }
)

@Composable
fun NativeAdImage(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String?,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) = AsyncImage(
    model = model,
    contentDescription = contentDescription,
    contentScale = contentScale,
    alignment = alignment,
    alpha = alpha,
    colorFilter = colorFilter,
    modifier = modifier
)

@Composable
fun NativeAdMediaView(
    modifier: Modifier = Modifier,
    setup: (MediaView) -> Unit
) = AndroidView(
    modifier = modifier,
    factory = {
        MediaView(it)
    },
    update = {
        setup(it)
    }
)

@Composable
fun NativeAdMediaView(
    modifier: Modifier = Modifier,
    nativeAdView: NativeAdView,
    mediaContent: MediaContent,
    scaleType: ScaleType = ScaleType.FIT_CENTER
) = AndroidView(
    modifier = modifier,
    factory = {
        MediaView(it).apply {
            minimumHeight = 120.dp.value.roundToInt()
            minimumWidth = 120.dp.value.roundToInt()
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    },
    update = {
        nativeAdView.mediaView = it
        nativeAdView.mediaView?.mediaContent = mediaContent
        nativeAdView.mediaView?.setImageScaleType(scaleType)
    }
)

@Composable
fun rememberCustomNativeAdState(
    adUnit: String,
    adListener: AdListener? = null,
    nativeAdOptions: NativeAdOptions? = null
) = LocalContext.current.let {
    remember(adUnit) {
        NativeAdState(
            context = it,
            adUnit = adUnit,
            adListener = adListener,
            adOptions = nativeAdOptions
        )
    }
}

class NativeAdState(
    context: Context,
    adUnit: String,
    adListener: AdListener?,
    adOptions: NativeAdOptions?
) {

    val nativeAd: MutableStateFlow<NativeAd?> = MutableStateFlow(null)

    init {
        AdLoader.Builder(context, adUnit).let {
            if (adOptions != null) {
                it.withNativeAdOptions(adOptions)
            } else {
                it
            }
        }.let {
            if (adListener != null) {
                it.withAdListener(adListener)
            } else {
                it
            }
        }.forNativeAd { ad ->
            nativeAd.update { ad }
        }.build().loadAd(AdRequest.Builder().build())
    }
}