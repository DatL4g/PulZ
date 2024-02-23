package dev.datlag.gamechanger.ui.custom

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.android.gms.ads.nativead.NativeAd
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NativeAdCard(
    nativeAd: NativeAd,
    modifier: Modifier = Modifier
) {
    NativeAdViewCompose(
        modifier = modifier
    ) { nativeAdView ->
        SideEffect {
            nativeAdView.setNativeAd(nativeAd)
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        NativeAdView(
                            modifier = Modifier.size(56.dp),
                            getView = {
                                nativeAdView.iconView = it
                            }
                        ) {
                            NativeAdImage(
                                model = nativeAd.icon?.uri ?: nativeAd.icon?.drawable,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(MaterialTheme.shapes.small)
                            )
                        }
                        Column(
                            modifier = Modifier.weight(0.7F),
                            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically)
                        ) {
                            nativeAd.headline?.ifBlank { null }?.let { headline ->
                                NativeAdView(
                                    modifier = Modifier.fillMaxWidth(),
                                    getView = {
                                        nativeAdView.headlineView = it
                                    }
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = headline,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                maxItemsInEachRow = 2,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
                            ) {
                                nativeAd.store?.ifBlank { null }?.let { store ->
                                    NativeAdView(
                                        getView = {
                                            nativeAdView.storeView = it
                                        }
                                    ) {
                                        Text(
                                            text = store,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                                nativeAd.price?.ifBlank { null }?.let { price ->
                                    NativeAdView(
                                        getView = {
                                            nativeAdView.priceView = it
                                        }
                                    ) {
                                        Text(text = price)
                                    }
                                }
                                nativeAd.starRating?.roundToInt()?.let { rating ->
                                    NativeAdView(
                                        getView = {
                                            nativeAdView.starRatingView = it
                                        }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            repeat(rating) {
                                                Icon(
                                                    imageVector = Icons.Default.Star,
                                                    contentDescription = null
                                                )
                                            }
                                            repeat(5 - rating) {
                                                Icon(
                                                    imageVector = Icons.Default.StarOutline,
                                                    contentDescription = null
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    nativeAd.body?.ifBlank { null }?.let { body ->
                        NativeAdView(
                            modifier = Modifier.fillMaxWidth(),
                            getView = {
                                nativeAdView.bodyView = it
                            }
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = body
                            )
                        }
                    }
                    nativeAd.mediaContent?.let { media ->
                        NativeAdMediaView(
                            modifier = Modifier
                                .defaultMinSize(
                                    minWidth = 120.dp,
                                    minHeight = 120.dp
                                )
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.small)
                                .horizontalScroll(rememberScrollState()),
                            nativeAdView = nativeAdView,
                            mediaContent = media
                        )
                    }
                    nativeAd.callToAction?.ifBlank { null }?.let { action ->
                        NativeAdView(
                            modifier = Modifier.fillMaxWidth(),
                            getView = {
                                nativeAdView.callToActionView = it
                            }
                        ) {
                            Button(
                                onClick = {
                                    nativeAdView.callToActionView?.performClick()
                                }
                            ) {
                                Text(text = action)
                            }
                        }
                    }
                }
            }
        }
        DisposableEffect(nativeAd) {
            onDispose {
                nativeAd.destroy()
            }
        }
    }
}