package com.mburakcakir.cryptopricetracker.data.repository

import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.network.CryptoApi
import com.mburakcakir.cryptopricetracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoinRepositoryImpl : CoinRepository {

    private val retrofitClient = CryptoApi.retrofitService

    override suspend fun getAllCoins(): Flow<Resource<List<CoinMarketItem>>> = flow {
        try {
            val response = retrofitClient.getAllCoins()
            if (response.isSuccessful) {
                response.body()?.apply {
                    emit(Resource.Success(this))
                }
            }
        } catch (e: Exception) {
            emit(Resource.Error(e))
            e.printStackTrace()
        }
    }
}