package com.mburakcakir.cryptopricetracker.data.repository

import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getAllCoins(): Flow<Resource<List<CoinMarketItem>>>
    suspend fun getCoinByID(id: String): Flow<Resource<CoinDetailItem>>
}