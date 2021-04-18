package com.mburakcakir.cryptopricetracker.data.network

import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CryptoService {
    @GET("coins/markets?vs_currency=usd")
    suspend fun getAllCoins(): Response<List<CoinMarketItem>>

    @GET("coins/{id}")
    suspend fun getCoinByID(@Path("id") id: String): Response<CoinDetailItem>
}