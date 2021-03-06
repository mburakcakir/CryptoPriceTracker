package com.mburakcakir.cryptopricetracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mburakcakir.cryptopricetracker.util.Result

open class BaseViewModel : ViewModel() {

    val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

    val firebaseAuth = FirebaseAuth.getInstance()

}