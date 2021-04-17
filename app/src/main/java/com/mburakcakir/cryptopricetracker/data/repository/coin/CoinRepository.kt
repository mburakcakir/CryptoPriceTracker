package com.mburakcakir.cryptopricetracker.data.repository.coin

import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.util.Resource
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getAllCoins(): Flow<Resource<List<CoinMarketItem>>>
    suspend fun getCoinByID(id: String): Flow<Resource<CoinDetailItem>>
    suspend fun insertAllCoins(listCrypto: List<CoinMarketItem>): Flow<Resource<Boolean>>
    suspend fun getCoinsByParameter(parameter: String): Flow<Resource<List<CoinMarketItem>>>
}