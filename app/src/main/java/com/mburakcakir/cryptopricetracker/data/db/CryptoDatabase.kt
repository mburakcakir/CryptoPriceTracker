package com.mburakcakir.cryptopricetracker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mburakcakir.cryptopricetracker.data.db.dao.CryptoDao
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem

@Database(
    entities = [CoinMarketItem::class],
    version = 1,
    exportSchema = false
)
abstract class CryptoDatabase : RoomDatabase() {
    abstract fun cryptoDao(): CryptoDao
//
//    companion object {
//        @Volatile
//        private var cryptoDatabase: CryptoDatabase? = null
//
//        fun getDatabaseInstance(
//            context: Context
//        ): CryptoDatabase {
//            return cryptoDatabase ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    CryptoDatabase::class.java,
//                    "crypto_database"
//                )
//                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .build()
//                cryptoDatabase = instance
//                instance
//            }
//        }
//    }
}