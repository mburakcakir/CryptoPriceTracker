package com.mburakcakir.cryptopricetracker.ui.entry.register

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.ui.entry.EntryViewModel
import kotlinx.coroutines.launch

class RegisterViewModel : EntryViewModel() {

    fun insertUser(email: String, password: String) = viewModelScope.launch {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _resultEntry.postValue(true)
            }
            .addOnFailureListener { exception ->
                _resultEntry.postValue(false)
                Log.v("errorLogin", exception.toString())
            }
    }
}
