package com.mburakcakir.cryptopricetracker.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mburakcakir.cryptopricetracker.util.Result
import org.koin.core.component.KoinComponent

open class BaseViewModel : ViewModel(), KoinComponent {

    val _result = MutableLiveData<Result>()
    val result: LiveData<Result> = _result

}