package com.mburakcakir.cryptopricetracker.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mburakcakir.cryptopricetracker.data.model.FavouriteCoinModel
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel

class FavouriteCoinsViewModel : BaseViewModel() {

    private val _favouriteCoins = MutableLiveData<List<FavouriteCoinModel>>()
    val favouriteCoins: LiveData<List<FavouriteCoinModel>> = _favouriteCoins
    private val currentList: MutableList<FavouriteCoinModel> = mutableListOf()
    val db = Firebase.firestore
        .collection("Cryptocurrency")
        .document(firebaseAuth.currentUser.uid)
        .collection("listFavouriteCrypto")

    init {
        getAllFavourites()
    }

    private fun getAllFavourites() {
        db.get()
            .addOnSuccessListener { document ->
                val list = document.documents
                list.forEach {
                    val coinMarketItem = it.toObject(FavouriteCoinModel::class.java)
                    coinMarketItem?.let { favouriteCoinModel ->
                        currentList.add(favouriteCoinModel)
                    }
                }
                _favouriteCoins.value = currentList
            }
            .addOnFailureListener { exception ->
                exception
            }
    }
}