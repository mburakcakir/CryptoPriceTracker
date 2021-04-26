package com.mburakcakir.cryptopricetracker.data.repository

import com.mburakcakir.cryptopricetracker.data.db.dao.CryptoDao
import com.mburakcakir.cryptopricetracker.data.db.entity.CoinMarketEntity
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.network.CryptoService
import com.mburakcakir.cryptopricetracker.util.Resource
import com.mburakcakir.cryptopricetracker.util.getResourceByDatabaseRequest
import com.mburakcakir.cryptopricetracker.util.getResourceByNetworkRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val cryptoService: CryptoService,
    private val cryptoDao: CryptoDao
) : CoinRepository {

    override suspend fun getAllCoins(): Flow<Resource<List<CoinMarketItem>>> = flow {
        emit(getResourceByNetworkRequest { cryptoService.getAllCoins() })
    }

    override suspend fun getCoinByID(id: String): Flow<Resource<CoinDetailItem>> = flow {
        emit(getResourceByNetworkRequest { cryptoService.getCoinByID(id) })
    }

    override suspend fun insertAllCoins(listCrypto: List<CoinMarketEntity>): Flow<Resource<Unit>> =
        flow {
            emit(getResourceByDatabaseRequest { cryptoDao.insertAllCrypto(listCrypto) })
        }

    override suspend fun getCoinsByParameter(parameter: String): Flow<Resource<List<CoinMarketEntity>>> =
        flow {
            emit(getResourceByDatabaseRequest { cryptoDao.getCryptoByParameter(parameter) })
        }
}