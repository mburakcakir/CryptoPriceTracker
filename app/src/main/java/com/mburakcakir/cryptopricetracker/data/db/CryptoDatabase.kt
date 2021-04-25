package com.mburakcakir.cryptopricetracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mburakcakir.cryptopricetracker.data.db.dao.CryptoDao
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem

@Database(
    entities = [CoinMarketItem::class],
    version = 2,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
}