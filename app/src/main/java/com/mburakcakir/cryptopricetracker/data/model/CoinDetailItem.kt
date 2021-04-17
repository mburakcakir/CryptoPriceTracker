package com.mburakcakir.cryptopricetracker.data.model

import com.google.gson.annotations.SerializedName

data class CoinDetailItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("description")
    val description: Description,

    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String,

    @SerializedName("image")
    val image: Image,

    @SerializedName("market_data")
    val marketData: MarketData,

    @SerializedName("last_updated")
    val lastUpdated: String,
)