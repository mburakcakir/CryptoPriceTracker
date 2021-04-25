package com.mburakcakir.cryptopricetracker.ui.entry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mburakcakir.cryptopricetracker.ui.BaseViewModel
import com.mburakcakir.cryptopricetracker.util.Constants
import com.mburakcakir.cryptopricetracker.util.ValidationUtils
import com.mburakcakir.cryptopricetracker.util.enums.EntryState
import com.mburakcakir.cryptopricetracker.util.enums.EntryType

open class EntryViewModel : BaseViewModel() {
    private val _entryForm = MutableLiveData<EntryFormState>()
    val entryFormState: LiveData<EntryFormState> = _entryForm
    private val _errorPassword = MutableLiveData("")
    private val _errorEmail = MutableLiveData("")

    private lateinit var entryType: EntryType
    private var typeList: MutableList<String?> = mutableListOf()

    val _resultEntry = MutableLiveData<Boolean>()
    val resultEntry: LiveData<Boolean> = _resultEntry

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
        typeList = when (entryType) {
            EntryType.LOGIN -> mutableListOf(_errorEmail.value, _errorPassword.value)
            EntryType.REGISTER -> mutableListOf(_errorEmail.value, _errorPassword.value)
        }
    }

    fun setEntryType(entryType: EntryType) {
        this.entryType = entryType
    }

    private fun isPasswordValid(text: String) = ValidationUtils().isPasswordValid(text)
    private fun isEmailValid(text: String) = ValidationUtils().isEmailValid(text)
}