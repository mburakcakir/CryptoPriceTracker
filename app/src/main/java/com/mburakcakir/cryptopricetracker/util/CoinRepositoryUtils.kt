package com.mburakcakir.cryptopricetracker.util

import retrofit2.Response

suspend fun <T> getResourceByNetworkRequest(request: suspend () -> Response<T>): Resource<T> {
    try {
        val response = request()
        if (response.isSuccessful) {
            response.body()?.apply {
                return Resource.Success(this)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return Resource.Error(e)
    }

    return Resource.Loading()
}

suspend fun <T> getResourceByDatabaseRequest(request: suspend () -> T): Resource<T> {
    try {
        val result = request()
        result?.let {
            return Resource.Success(result)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return Resource.Error(e)
    }
    return Resource.Loading()
}