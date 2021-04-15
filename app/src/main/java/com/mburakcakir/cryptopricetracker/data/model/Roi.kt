package com.mburakcakir.cryptopricetracker.data.model

data class Roi(
    val currency: String,
    val percentage: Double,
    val times: Double
)