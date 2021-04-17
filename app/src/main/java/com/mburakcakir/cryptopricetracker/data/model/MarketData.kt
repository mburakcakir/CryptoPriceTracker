package com.mburakcakir.cryptopricetracker.data.model

import com.google.gson.annotations.SerializedName

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: CurrentPrice,

    @SerializedName("last_updated")
    val lastUpdated: String,

    @SerializedName("price_change_24h")
    val priceChange24h: Double,
)