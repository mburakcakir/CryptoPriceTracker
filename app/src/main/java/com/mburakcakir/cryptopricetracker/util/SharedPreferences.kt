package com.mburakcakir.cryptopricetracker.util

import android.content.Context

class SharedPreferences(context: Context) {
    private var sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, 0)
    private val editor = sharedPreferences.edit()

    fun saveRefreshInterval(duration: Int) {
        editor.apply {
            putString(Constants.REFRESH_INTERVAL, duration.toString())
            commit()
        }
    }

    fun getRefreshInterval(): String? {
        return sharedPreferences.getString(Constants.REFRESH_INTERVAL, null)
    }
}