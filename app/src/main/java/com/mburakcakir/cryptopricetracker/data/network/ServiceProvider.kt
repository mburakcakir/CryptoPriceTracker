package com.mburakcakir.cryptopricetracker.data.network

import com.mburakcakir.cryptopricetracker.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BuildConfig.API_URL)
    .build()

object CryptoApi {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}
