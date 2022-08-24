package com.ad_manager

import android.content.Context
import android.content.SharedPreferences

object AdsSPManager {
    private const val TIME_KEY_ADS = "TIME_KEY_ADS"
    private const val TIME_DELAY = (69 * 1000)

    private const val PREMIUM_UPGRADE = "ZIP_PREMIUM_UPGRADE"

    private fun getSP(c: Context): SharedPreferences {
        return c.getSharedPreferences(c.packageName, Context.MODE_PRIVATE)
    }

    fun updateInterstitialShowTime(c: Context) {
        val sp = getSP(c)
        sp.edit().putLong(TIME_KEY_ADS, System.currentTimeMillis() + TIME_DELAY).apply()
    }

    fun isTimeValid(c: Context?): Boolean {
        return c?.let {
            val sp = getSP(it)
            val time = sp.getLong(TIME_KEY_ADS, 0L)
            time < System.currentTimeMillis()
        } ?: false
    }

    fun upgradePremium(c: Context) {
        getSP(c)
            .edit()
            .putBoolean(PREMIUM_UPGRADE, true)
            .apply()
    }

    fun isPremium(context: Context): Boolean {
        return getSP(context).getBoolean(PREMIUM_UPGRADE, false)
    }
}