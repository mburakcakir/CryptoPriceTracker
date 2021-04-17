package com.mburakcakir.cryptopricetracker.ui.entry.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mburakcakir.cryptopricetracker.ui.entry.EntryViewModel
import com.mburakcakir.cryptopricetracker.ui.entry.ResultEntry
import kotlinx.coroutines.launch

class LoginViewModel : EntryViewModel() {

//    fun login(username: String, password: String) = viewModelScope.launch {
//        userRepository.getUserByUsername(username, password)
//            .onStart { _result.postValue(ResultEntry(loading = "Giriş Yapılıyor...")) }
//            .collect {
//                when (it.status) {
//                    Status.SUCCESS -> checkLogin(it.data)
//                    Status.ERROR -> _result.postValue(ResultEntry(error = "Giriş Başarısız."))
//                }
//            }
//    }

    private val _isVerifiedSent = MutableLiveData<Boolean>()
    val isVerifiedSent: LiveData<Boolean> = _isVerifiedSent


    fun login(email: String, password: String) = viewModelScope.launch {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            _result.postValue(ResultEntry(success = "Giriş Yapıldı"))
        }
            .addOnFailureListener {
                _result.postValue(ResultEntry(error = "Giriş Yapılamadı"))
            }
    }

    fun checkIfUserVerified(): Boolean {
        firebaseAuth.currentUser?.let {
            return it.isEmailVerified
        }
        return false
    }

    fun sendEmailVerify() {
        if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser.sendEmailVerification()

                .addOnSuccessListener {
                    _isVerifiedSent.postValue(true)
                }

                .addOnFailureListener {
                    _isVerifiedSent.postValue(false)
                }
        }
    }

}