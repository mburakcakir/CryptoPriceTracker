package com.mburakcakir.cryptopricetracker.ui.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.mburakcakir.cryptopricetracker.utils.Constants
import com.mburakcakir.cryptopricetracker.utils.EntryState
import com.mburakcakir.cryptopricetracker.utils.EntryType
import com.mburakcakir.cryptopricetracker.utils.ValidationUtils

open class EntryViewModel : ViewModel() {
    private val _entryForm = MutableLiveData<EntryFormState>()
    val entryFormState: LiveData<EntryFormState> = _entryForm
    private val _errorPassword = MutableLiveData("")
    private val _errorEmail = MutableLiveData("")

    private lateinit var entryType: EntryType
    private var typeList: MutableList<String?> = mutableListOf()

    val _result = MutableLiveData<ResultEntry>()
    val resultEntry: LiveData<ResultEntry> = _result

    val firebaseAuth = FirebaseAuth.getInstance()

    init {
        _entryForm.value = EntryFormState()
    }

    fun isDataChanged(
        entryState: EntryState,
        text: String
    ) {
        when (entryState) {
            EntryState.EMAIL -> {
                _errorEmail.value =
                    if (!isEmailValid(text)) Constants.INVALID_EMAIL
                    else null
            }

            EntryState.PASSWORD -> {
                _errorPassword.value =
                    if (!isPasswordValid(text)) Constants.INVALID_PASSWORD
                    else null
            }

        }
        setEntryParameters()
        setEntryFormState()
    }

    private fun setEntryFormState() {
        _entryForm.value = EntryFormState(
            emailError = _errorEmail.value,
            passwordError = _errorPassword.value,
            isDataValid = isDataValid()
        )

    }

    private fun isDataValid() =
        mutableListOf<String>().apply {
            for (item in typeList)
                item?.let { this.add(it) }
        }.size == 0

    private fun setEntryParameters() {
        typeList = mutableListOf(_errorEmail.value, _errorPassword.value)
//            when (entryType) {
//            EntryType.LOGIN -> mutableListOf(_errorEmail.value, _errorPassword.value)
//            EntryType.REGISTER -> mutableListOf(_errorEmail.value, _errorPassword.value)
//        }
    }

    fun setEntryType(entryType: EntryType) {
        this.entryType = entryType
    }

    private fun isPasswordValid(text: String) = ValidationUtils().isPasswordValid(text)
    private fun isEmailValid(text: String) = ValidationUtils().isEmailValid(text)
}