package com.mburakcakir.cryptopricetracker.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.CoinMarketItem
import com.mburakcakir.cryptopricetracker.data.model.FavouriteCryptoModel
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

    private val _isAddedFavourite = MutableLiveData<Boolean>()
    val isAddedFavourite: LiveData<Boolean> = _isAddedFavourite

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

    fun addToFavourites(baseCoin: CoinMarketItem) {
        val favouriteCryptoModel = FavouriteCryptoModel(
            baseCoin.cryptoID,
            baseCoin.cryptoImage,
            baseCoin.name,
            baseCoin.symbol
        )

        val db = Firebase.firestore
            .collection("Cryptocurrency")
            .document(FirebaseAuth.getInstance().currentUser.uid)
            .collection("listFavouriteCrypto")
            .document(baseCoin.cryptoID)

        db.set(favouriteCryptoModel)
            .addOnSuccessListener {
                _isAddedFavourite.postValue(true)
            }
            .addOnFailureListener {
                _isAddedFavourite.postValue(false)
            }

    }

}