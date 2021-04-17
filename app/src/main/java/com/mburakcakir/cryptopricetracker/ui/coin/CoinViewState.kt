package com.mburakcakir.cryptopricetracker.ui.coin

import android.view.View
import com.mburakcakir.cryptopricetracker.util.enums.Status

class CoinViewState(private val status: Status) {
    fun getProgressBarVisibility() = if (status == Status.LOADING) View.VISIBLE else View.GONE
    fun getRecyclerViewVisibility() = if (status == Status.SUCCESS) View.VISIBLE else View.GONE
    fun getErrorMessageVisibility() = if (status == Status.ERROR) View.VISIBLE else View.GONE
}