package com.mburakcakir.cryptopricetracker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem

@Dao
interface CryptoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCrypto(listCrypto: List<CoinMarketItem>)

    @Query("SELECT * FROM tbl_coin_list WHERE name LIKE :searchParameter OR symbol LIKE :searchParameter")
    suspend fun getCryptoByParameter(searchParameter: String): List<CoinMarketItem>
}