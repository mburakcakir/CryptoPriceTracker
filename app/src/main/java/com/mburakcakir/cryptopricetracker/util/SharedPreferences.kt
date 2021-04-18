package com.mburakcakir.cryptopricetracker.util

import android.content.Context

class SharedPreferences(context: Context) {
    private var sharedPreferences = context.getSharedPreferences(PREF_NAME, 0)
    private val editor = sharedPreferences.edit()

    companion object {
        const val PREF_NAME: String = "CryptoPriceTracker"
        const val REFRESH_INTERVAL: String = "REFRESH_INTERVAL"
    }

    fun saveRefreshInterval(duration: Int) {
        editor.apply {
            putString(REFRESH_INTERVAL, duration.toString())
            commit()
        }
    }

    fun getRefreshInterval(): String? {
        return sharedPreferences.getString(REFRESH_INTERVAL, null)
    }
}