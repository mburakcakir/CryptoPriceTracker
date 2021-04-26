package com.mburakcakir.cryptopricetracker.util

import android.util.Patterns
import java.util.regex.Pattern

class ValidationUtils {
    fun isEmailValid(email: String): Boolean {
        return if (email.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    fun isPasswordValid(password: String): Boolean {
        val textPattern: Pattern = Pattern.compile(Constants.PASSWORD_PATTERN)
        return textPattern.matcher(password).matches() && password.length > 5
    }

    fun isUserNameValid(username: String): Boolean {
        return username.length > 3
    }
}