package com.mburakcakir.cryptopricetracker.di

import android.content.Context
import androidx.room.Room
import com.mburakcakir.cryptopricetracker.data.db.CryptoDatabase
import com.mburakcakir.cryptopricetracker.data.db.dao.CryptoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CryptoDatabase =
        Room.databaseBuilder(context, CryptoDatabase::class.java, "crypto.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCryptoDao(database: CryptoDatabase): CryptoDao = database.cryptoDao()

}