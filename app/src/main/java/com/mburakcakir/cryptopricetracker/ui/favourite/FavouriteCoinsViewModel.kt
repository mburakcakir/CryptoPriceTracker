package com.mburakcakir.cryptopricetracker.ui.favourite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mburakcakir.cryptopricetracker.data.model.FavouriteCoinModel
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel

class FavouriteCoinsViewModel : BaseViewModel() {

    private val _favouriteCoins = MutableLiveData<List<FavouriteCoinModel>>()
    val favouriteCoins: LiveData<List<FavouriteCoinModel>> = _favouriteCoins

    private val _coinState = MutableLiveData<Boolean>()
    val coinState: LiveData<Boolean> = _coinState

    private val favouriteCoinsList: MutableList<FavouriteCoinModel> = mutableListOf()

    private val db = Firebase.firestore
        .collection("Cryptocurrency")
        .document(firebaseAuth.currentUser.uid)
        .collection("listFavouriteCrypto")

    fun getAllFavourites() {
        db.get()
            .addOnSuccessListener { document ->
                val list = document.documents
                list.forEach {
                    val coinMarketItem = it.toObject(FavouriteCoinModel::class.java)
                    coinMarketItem?.let { favouriteCoinModel ->
                        favouriteCoinsList.add(favouriteCoinModel)
                    }
                }
                _coinState.value = true
                _favouriteCoins.value = favouriteCoinsList
            }
            .addOnFailureListener { exception ->
                _coinState.value = false
                Log.v("exceptionFavourites", exception.toString())
            }
    }
}