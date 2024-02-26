package dev.datlag.gamechanger.other

import android.app.Activity
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

actual class ConsentInfo(private val activity: Activity) {

    private var isMobileAdsInitialized = AtomicBoolean(false)

    private val consentInformation = UserMessagingPlatform.getConsentInformation(activity)
    private val params = ConsentRequestParameters.Builder().setConsentDebugSettings(
        ConsentDebugSettings.Builder(activity)
            .addTestDeviceHashedId("47786B5A1CA9C70693FEAE00BF176FE8")
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .setForceTesting(true)
            .build()
    ).build()

    actual val privacy: Boolean
        get() = consentInformation.privacyOptionsRequirementStatus == ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED

    actual fun initialize() {
        consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            {
                // success
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity
                ) {
                    // dismiss
                    initializeMobileAds()
                }
            },
            {
                // error
                initializeMobileAds(true)
            }
        )
    }

    actual fun reset() {
        consentInformation.reset()
    }

    actual fun showPrivacyForm() {
        UserMessagingPlatform.showPrivacyOptionsForm(
            activity
        ) {
            // dismiss
            initializeMobileAds()
        }
    }

    private fun initializeMobileAds(force: Boolean = false) {
        if (consentInformation.canRequestAds() || force) {
            if (isMobileAdsInitialized.get()) {
                return
            }

            MobileAds.initialize(activity)
            isMobileAdsInitialized.set(true)
        }
    }
}