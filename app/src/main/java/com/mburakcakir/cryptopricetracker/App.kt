package com.mburakcakir.cryptopricetracker

import android.app.Application
import com.mburakcakir.cryptopricetracker.utils.NetworkController

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        NetworkController(this).apply {
            startNetworkCallback()
        }
    }
}