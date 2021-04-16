package com.mburakcakir.cryptopricetracker

import android.app.Application
import com.mburakcakir.cryptopricetracker.di.networkModule
import com.mburakcakir.cryptopricetracker.di.repositoryModule
import com.mburakcakir.cryptopricetracker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(viewModelModule, repositoryModule, networkModule))
        }
    }
}