package com.mburakcakir.cryptopricetracker.data.model

data class CoinDetailItem(
    val id: String,
    val name: String,
    val symbol: String,
    val description: Description,
    val hashing_algorithm: String,
    val image: Image,
    val market_data: MarketData,
    val last_updated: String

)