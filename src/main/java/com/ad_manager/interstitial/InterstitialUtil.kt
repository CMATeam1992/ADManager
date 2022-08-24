package com.ad_manager.interstitial

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ad_manager.AdsSPManager

class InterstitialUtil {
    private var isReloaded: Boolean = false

    var onDismissAds: (() -> Unit)? = null

    companion object {
        private var interstitialAd: InterstitialAd? = null
        fun getInstance(): InterstitialUtil = InterstitialUtil()
    }

    fun loadAd(
        context: Context,
        adUnitId: String
    ) {
        if (!AdsSPManager.isPremium(context)) {
            val adRequest = AdRequest.Builder().build()
            InterstitialAd.load(
                context,
                adUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        if (!isReloaded) {
                            isReloaded = true
                            loadAd(context, adUnitId)
                        }
                    }

                    override fun onAdLoaded(ad: InterstitialAd) {
                        interstitialAd = ad
                    }
                }
            )
        }
    }

    fun forceShowInterstitial(activity: Activity, adUnitIdNext: String) {
        if (AdsSPManager.isPremium(activity)) {
            onDismissAds?.invoke()
            return
        }

        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                    loadAd(activity, adUnitIdNext)
                    onDismissAds?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                    onDismissAds?.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is dismissed.
                    isReloaded = true
                    Log.e("TAG", "onAdShowedFullScreenContent: ")
                }
            }
            interstitialAd?.show(activity)
        } else {
            onDismissAds?.invoke()
        }
    }

    fun showInterstitial(activity: Activity?, adUnitIdNext: String) {
        if (activity == null) {
            onDismissAds?.invoke()
            return
        }

        if (AdsSPManager.isPremium(activity)) {
            onDismissAds?.invoke()
            return
        }

        if (interstitialAd != null && AdsSPManager.isTimeValid(activity)) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                    loadAd(activity, adUnitIdNext)
                    AdsSPManager.updateInterstitialShowTime(activity)
                    onDismissAds?.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                    // Don't forget to set the ad reference to null so you
                    // don't show the ad a second time.
                    interstitialAd = null
                    onDismissAds?.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is dismissed.
                    isReloaded = true
                    Log.e("TAG", "onAdShowedFullScreenContent: ")
                }
            }
            interstitialAd?.show(activity)
        } else {
            onDismissAds?.invoke()
        }
    }
}