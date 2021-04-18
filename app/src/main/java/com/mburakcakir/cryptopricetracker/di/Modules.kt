package com.mburakcakir.cryptopricetracker.di

import android.app.Application
import androidx.room.Room
import com.mburakcakir.cryptopricetracker.BuildConfig
import com.mburakcakir.cryptopricetracker.data.db.CryptoDatabase
import com.mburakcakir.cryptopricetracker.data.db.dao.CryptoDao
import com.mburakcakir.cryptopricetracker.data.network.CryptoService
import com.mburakcakir.cryptopricetracker.data.repository.coin.CoinRepository
import com.mburakcakir.cryptopricetracker.data.repository.coin.CoinRepositoryImpl
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel
import com.mburakcakir.cryptopricetracker.ui.coin.CoinViewModel
import com.mburakcakir.cryptopricetracker.ui.detail.CoinDetailViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.EntryViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.login.LoginViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.register.RegisterViewModel
import com.mburakcakir.cryptopricetracker.ui.favourite.FavouriteCoinsViewModel
import org.koin.android.ext.koin.androidApplication
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
    viewModel { FavouriteCoinsViewModel() }
    viewModel { EntryViewModel() }
    viewModel { BaseViewModel() }
}

val repositoryModule = module {
    single { CoinRepositoryImpl(get(), get()) } binds arrayOf(CoinRepository::class)
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

val databaseModule = module {
    fun provideDatabase(application: Application): CryptoDatabase {
        return Room.databaseBuilder(
            application,
            CryptoDatabase::class.java,
            "crypto_database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCryptoDao(database: CryptoDatabase): CryptoDao {
        return database.cryptoDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideCryptoDao(get()) }
}