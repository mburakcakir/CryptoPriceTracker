package com.mburakcakir.cryptopricetracker.ui.coin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.db.entity.CoinMarketEntity
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.repository.CoinRepository
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel
import com.mburakcakir.cryptopricetracker.util.Resource
import com.mburakcakir.cryptopricetracker.util.Result
import com.mburakcakir.cryptopricetracker.util.enums.Status
import com.mburakcakir.cryptopricetracker.util.format
import com.mburakcakir.cryptopricetracker.util.getCoinMarketEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(private val coinRepository: CoinRepository) :
    BaseViewModel() {

    private val _allCoins = MutableLiveData<Resource<List<CoinMarketItem>>>()
    val allCoins: LiveData<Resource<List<CoinMarketItem>>> = _allCoins

    private val _coinByParameter = MutableLiveData<Resource<List<CoinMarketEntity>>>()
    val coinByParameter: LiveData<Resource<List<CoinMarketEntity>>> = _coinByParameter

    fun getCoinsByParameter(parameter: String) = viewModelScope.launch {
        coinRepository.getCoinsByParameter(parameter.format())
            .onStart {
                _result.value = Result(loading = R.string.loading)
            }
            .catch {
                Log.v("errorGetCoinByParameter", it.message.toString())
            }
            .collect {
                _coinByParameter.value = it
            }
    }

    fun getAllCoins() = viewModelScope.launch {
        coinRepository.getAllCoins()
            .onStart {
                _result.value = Result(loading = R.string.loading)
            }
            .catch {
                Log.v("errorGetAllCoins", it.message.toString())
            }
            .collect {
                _allCoins.value = it
            }
    }

    fun insertAllCoins(listCrypto: List<CoinMarketItem>) = viewModelScope.launch {
        val coinEntityList = getCoinMarketEntity(listCrypto)

        coinRepository.insertAllCoins(coinEntityList)
            .onStart {
                _result.value = Result(loading = R.string.coin_loading)
            }
            .collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.let {
                            Result(success = R.string.coin_success)
                        }
                    }
                    Status.ERROR -> Result(success = R.string.coin_error)
                }
            }
    }

    fun endSession() {
        firebaseAuth.signOut()
    }

    fun checkIfUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}