package com.mburakcakir.cryptopricetracker.ui.entry.register

import androidx.lifecycle.viewModelScope
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mburakcakir.cryptopricetracker.ui.entry.EntryViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.ResultEntry
import kotlinx.coroutines.launch

class RegisterViewModel : EntryViewModel() {
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    init {
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
    }

    fun insertUser(email: String, password: String) = viewModelScope.launch {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _result.postValue(ResultEntry(success = "Kayıt Olundu"))
            }
            .addOnFailureListener { exception ->
                _result.postValue(ResultEntry(error = exception.toString()))
            }
    }
}
