package com.mburakcakir.cryptopricetracker.data.repository

import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.network.CryptoServiceApi
import com.mburakcakir.cryptopricetracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CoinRepositoryImpl(private val clientApi: CryptoServiceApi) {

    private val retrofitClient = clientApi.getApiService()

    suspend fun getAllCoins(): Flow<Resource<List<CoinMarketItem>>> = flow {
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

    suspend fun getCoinByID(id: String): Flow<Resource<CoinDetailItem>> = flow {
        try {
            val response = retrofitClient.getCoinByID(id)
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