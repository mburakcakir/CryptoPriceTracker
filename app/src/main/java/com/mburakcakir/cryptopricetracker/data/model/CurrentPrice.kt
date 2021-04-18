package com.mburakcakir.cryptopricetracker.data.model

import com.google.gson.annotations.SerializedName

data class CurrentPrice(
    @SerializedName("try")
    val TRY: Double,

    @SerializedName("usd")
    val usd: Double,
)