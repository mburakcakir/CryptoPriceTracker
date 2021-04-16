package com.mburakcakir.cryptopricetracker.ui.coin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.repository.CoinRepositoryImpl
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel
import com.mburakcakir.cryptopricetracker.utils.Resource
import com.mburakcakir.cryptopricetracker.utils.Result
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CoinViewModel(private val coinRepository: CoinRepositoryImpl) : BaseViewModel() {

    private val _allCoins = MutableLiveData<Resource<List<CoinMarketItem>>>()
    val allCoins: LiveData<Resource<List<CoinMarketItem>>> = _allCoins

    init {
        getAllCoins()
    }

    fun getAllCoins() = viewModelScope.launch {
        coinRepository.getAllCoins()
            .onStart {
                _result.value = Result(loading = R.string.loading)
            }
            .catch {
                it.message
            }
            .collect {
                _allCoins.value = it
            }
    }

//    private fun getAllCoins() = viewModelScope.launch {
//        coinRepository.getAllCoins()
//            .onStart {
//                _result.value = Result(loading = R.string.loading)
//            }
//            .collect {
//                when (it.status) {
//                    Status.SUCCESS -> {
//                        it.data?.let { responseCoinItemList ->
//                            _result.value = Result(success = R.string.success)
//                            _coinInfo.value = responseCoinItemList
//                        }
//                    }
//                    Status.ERROR -> {
//                        _result.value = Result(error = R.string.error)
//                        Log.v("coinInfo", it.message.toString())
//                    }
//                }
//            }
//    }
}