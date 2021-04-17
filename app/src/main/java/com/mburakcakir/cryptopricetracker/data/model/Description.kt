package com.mburakcakir.cryptopricetracker.data.model

import com.google.gson.annotations.SerializedName

data class Description(
    @SerializedName("en")
    val en: String,
)