package com.mburakcakir.cryptopricetracker.ui.entry

data class EntryFormState(
//    val nameError: String? = null,
//    var usernameError: String? = null,
    var passwordError: String? = null,
    var emailError: String? = null,
    val isDataValid: Boolean = false
)