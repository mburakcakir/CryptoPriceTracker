package com.mburakcakir.cryptopricetracker.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.data.model.CoinDetailItem
import com.mburakcakir.cryptopricetracker.data.model.FavouriteCoinModel
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

    private val _isFavouriteAdded = MutableLiveData<Boolean>()
    val isFavouriteAdded: LiveData<Boolean> = _isFavouriteAdded

    private val _isFavouriteDeleted = MutableLiveData<Boolean>()
    val isFavouriteDeleted: LiveData<Boolean> = _isFavouriteDeleted

    private val _isFavourite = MutableLiveData<Boolean>()
    val isFavourite: LiveData<Boolean> = _isFavourite

    val db = Firebase.firestore
        .collection("Cryptocurrency")
        .document(firebaseAuth.currentUser.uid)
        .collection("listFavouriteCrypto")

    fun getCoinByID(id: String) = viewModelScope.launch {
        coinRepository.getCoinByID(id)
            .onStart {
                _result.value = Result(loading = R.string.loading)
            }
            .catch {
                Log.v("errorGetCoinByID", it.message.toString())
            }
            .collect {
                _coinInfo.value = it
            }
    }

    fun addToFavourites(coinDetail: CoinDetailItem) {
        val favouriteCryptoModel = FavouriteCoinModel(
            coinDetail.id,
            coinDetail.image.small,
            coinDetail.name,
            coinDetail.symbol
        )

        val favouriteDocument = db.document(coinDetail.id)

        favouriteDocument.set(favouriteCryptoModel)
            .addOnSuccessListener {
                _isFavouriteAdded.postValue(true)
            }
            .addOnFailureListener {
                _isFavouriteAdded.postValue(false)
            }

    }

    fun isFavourite(cryptoID: String) {
        val favouriteDocument = db.document(cryptoID)

        favouriteDocument.get()
            .addOnSuccessListener { document ->
                _isFavourite.value = document.exists()
            }
            .addOnFailureListener { exception ->
                _isFavourite.value = false
            }
    }


    fun getAllFavourites() {
        db.get()
            .addOnSuccessListener { document ->
                Log.v("documents", document.documents.toString())
            }
            .addOnFailureListener { exception ->
                exception
            }
    }

    fun deleteFavourite(cryptoID: String) {
        val favouriteDocument = db.document(cryptoID)

        favouriteDocument
            .delete()
            .addOnSuccessListener { _isFavouriteDeleted.postValue(true) }
            .addOnFailureListener { _isFavouriteDeleted.postValue(false) }
    }


}