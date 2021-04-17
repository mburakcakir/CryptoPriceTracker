package com.mburakcakir.cryptopricetracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.repository.coin.CoinRepositoryImpl
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel
import com.mburakcakir.cryptopricetracker.util.Resource
import com.mburakcakir.cryptopricetracker.util.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CoinDetailViewModel(private val coinRepository: CoinRepositoryImpl) : BaseViewModel() {

    private val _coinInfo = MutableLiveData<Resource<CoinDetailItem>>()
    val coinInfo: LiveData<Resource<CoinDetailItem>> = _coinInfo

    fun getCoinByID(id: String) = viewModelScope.launch {
        coinRepository.getCoinByID(id)
            .onStart {
                _result.value = Result(loading = R.string.loading)
            }
            .catch {
                it.message
            }
            .collect {
                _coinInfo.value = it
            }
    }

    fun formatUpdatedTime(updateTime: String): String {
        return "${updateTime.substring(0, 10)}, ${updateTime.substring(11, 19)}"
    }

    fun formatPriceChange(priceChange: Double): Double {
        return String.format("%.2f", priceChange).replace(",", ".").toDouble()
    }

    fun setFavouriteMessage(isFavourite: Boolean): String {
        return if (isFavourite) "Added" else "Removed"
    }

}