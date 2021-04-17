package com.mburakcakir.cryptopricetracker.di

import com.mburakcakir.cryptopricetracker.BuildConfig
import com.mburakcakir.cryptopricetracker.data.network.CryptoService
import com.mburakcakir.cryptopricetracker.data.repository.coin.CoinRepository
import com.mburakcakir.cryptopricetracker.data.repository.coin.CoinRepositoryImpl
import com.mburakcakir.cryptopricetracker.ui.coin.CoinViewModel
import com.mburakcakir.cryptopricetracker.ui.detail.CoinDetailViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.login.LoginViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val viewModelModule = module {
    viewModel { CoinViewModel(get()) }
    viewModel { CoinDetailViewModel(get()) }
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel() }
}

val repositoryModule = module {
    single { CoinRepositoryImpl(get()) } binds arrayOf(CoinRepository::class)
//    single { UserRepositoryImpl(get()) } binds arrayOf(UserRepository::class)
}

val networkModule = module {
    single {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.API_URL)
            .build()
    }

    single { get<Retrofit>().create(CryptoService::class.java) }
}