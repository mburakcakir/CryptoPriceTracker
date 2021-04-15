package com.mburakcakir.cryptopricetracker.data.model

data class MarketData(
    val current_price: CurrentPrice,
    val last_updated: String,
    val market_cap_change_24h: Long,
    val market_cap_change_percentage_24h: Double,
    val price_change_24h: Double,
    val price_change_24H_in_currency: PriceChange24hInCurrency,
)