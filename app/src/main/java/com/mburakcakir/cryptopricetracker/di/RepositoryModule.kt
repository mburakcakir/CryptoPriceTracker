package com.mburakcakir.cryptopricetracker.di

import com.mburakcakir.cryptopricetracker.data.repository.CoinRepository
import com.mburakcakir.cryptopricetracker.data.repository.CoinRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideCoinRepository(repository: CoinRepositoryImpl): CoinRepository
}