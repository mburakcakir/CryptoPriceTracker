package com.mburakcakir.cryptopricetracker.data.network

import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("coins/markets?vs_currency=usd")
    suspend fun getAllCoins(): Response<List<CoinMarketItem>>
}