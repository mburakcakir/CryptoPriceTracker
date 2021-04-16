package com.mburakcakir.cryptopricetracker.di

import com.mburakcakir.cryptopricetracker.data.network.CryptoServiceApi
import com.mburakcakir.cryptopricetracker.data.repository.CoinRepositoryImpl
import com.mburakcakir.cryptopricetracker.ui.coin.CoinViewModel
import com.mburakcakir.cryptopricetracker.ui.detail.CoinDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CoinViewModel(get()) }
    viewModel { CoinDetailViewModel(get()) }
}

val repositoryModule = module {
    single { CoinRepositoryImpl(get()) }
}

val networkModule = module {
    single { CryptoServiceApi() }
}