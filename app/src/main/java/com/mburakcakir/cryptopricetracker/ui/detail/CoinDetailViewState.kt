package com.mburakcakir.cryptopricetracker.ui.detail

import android.view.View
import com.mburakcakir.cryptopricetracker.util.enums.Status

class CoinDetailViewState(private val status: Status) {
    fun getProgressBarVisibility() = if (status == Status.LOADING) View.VISIBLE else View.GONE
    fun getViewVisibility() = if (status == Status.SUCCESS) View.VISIBLE else View.GONE
//    fun getErrorMessageVisibility() = if (status == Status.ERROR) View.VISIBLE else View.GONE
}