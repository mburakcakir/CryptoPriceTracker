package com.mburakcakir.cryptopricetracker.ui.entry.register

import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.ui.entry.EntryViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.ResultEntry
import kotlinx.coroutines.launch

class RegisterViewModel : EntryViewModel() {

    fun insertUser(email: String, password: String) = viewModelScope.launch {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _resultEntry.postValue(ResultEntry(success = "Kayıt Olundu"))
            }
            .addOnFailureListener { exception ->
                _resultEntry.postValue(ResultEntry(error = exception.toString()))
            }
    }
}
