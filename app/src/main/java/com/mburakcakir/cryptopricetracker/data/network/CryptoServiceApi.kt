package com.mburakcakir.cryptopricetracker.data.network

import com.mburakcakir.cryptopricetracker.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CryptoServiceApi {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BuildConfig.API_URL)
        .build()

    fun getApiService(): CryptoService {
        return retrofit.create(CryptoService::class.java)
    }

}


//object CryptoApi {
//    val retrofitService: CryptoService by lazy { retrofit.create(CryptoService::class.java) }
//}
