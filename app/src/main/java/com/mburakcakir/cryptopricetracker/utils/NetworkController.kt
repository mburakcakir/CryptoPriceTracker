package com.mburakcakir.cryptopricetracker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.MutableLiveData

class NetworkController(context: Context) {

    private var _isNetworkConnected = MutableLiveData<Boolean>()
    var isNetworkConnected = _isNetworkConnected

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            _isNetworkConnected.postValue(true)
        }

        override fun onLost(network: Network) {
            _isNetworkConnected.postValue(false)
        }
    }

    fun startNetworkCallback() {
        val builder = NetworkRequest.Builder()

        // API 24 and above (API 24 ve yukarısı)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
        // API 23 ve below (API 23 ve aşağısı)
        else {
            connectivityManager.registerNetworkCallback(
                builder.build(), networkCallback
            )
        }
    }

    fun stopNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(ConnectivityManager.NetworkCallback())
    }

}

