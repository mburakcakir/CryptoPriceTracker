package com.mburakcakir.cryptopricetracker.data.repository

import com.mburakcakir.cryptopricetracker.data.db.dao.CryptoDao
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.network.CryptoService
import com.mburakcakir.cryptopricetracker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val cryptoService: CryptoService,
    private val cryptoDao: CryptoDao
) : CoinRepository {

    override suspend fun getAllCoins(): Flow<Resource<List<CoinMarketItem>>> = flow {
        try {
            val response = cryptoService.getAllCoins()
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

    override suspend fun getCoinByID(id: String): Flow<Resource<CoinDetailItem>> = flow {
        try {
            val response = cryptoService.getCoinByID(id)
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

    override suspend fun insertAllCoins(listCrypto: List<CoinMarketItem>) = flow {
        try {
            cryptoDao.insertAllCrypto(listCrypto)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e))
            e.printStackTrace()
        }
    }

    override suspend fun getCoinsByParameter(parameter: String): Flow<Resource<List<CoinMarketItem>>> =
        flow {
            try {
                emit(Resource.Success(cryptoDao.getCryptoByParameter(parameter)))
            } catch (e: Exception) {
                emit(Resource.Error(e))
                e.printStackTrace()
            }
        }
}