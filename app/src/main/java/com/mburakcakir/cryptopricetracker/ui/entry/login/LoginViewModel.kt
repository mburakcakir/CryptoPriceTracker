package com.mburakcakir.cryptopricetracker.ui.entry.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.ui.entry.EntryViewModel
import kotlinx.coroutines.launch

class LoginViewModel : EntryViewModel() {

    private val _isVerifiedSent = MutableLiveData<Boolean>()
    val isVerifiedSent: LiveData<Boolean> = _isVerifiedSent

    fun login(email: String, password: String) = viewModelScope.launch {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _resultEntry.postValue(true)
            }.addOnFailureListener { exception ->
                _resultEntry.postValue(false)
                Log.v("errorLogin", exception.toString())
            }
    }

    fun checkIfUserVerified(): Boolean {
        firebaseAuth.currentUser?.let {
            return it.isEmailVerified
        }
        return false
    }

    fun sendEmailVerify() {
        firebaseAuth.currentUser?.let {
            it.sendEmailVerification()
                .addOnSuccessListener {
                    _isVerifiedSent.postValue(true)
                }

                .addOnFailureListener {
                    _isVerifiedSent.postValue(false)
                }
        }
    }

}