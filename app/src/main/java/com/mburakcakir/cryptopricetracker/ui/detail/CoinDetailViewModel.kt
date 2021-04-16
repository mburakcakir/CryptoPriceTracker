package com.mburakcakir.cryptopricetracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.repository.CoinRepositoryImpl
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel
import com.mburakcakir.cryptopricetracker.utils.Resource
import com.mburakcakir.cryptopricetracker.utils.Result
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
}