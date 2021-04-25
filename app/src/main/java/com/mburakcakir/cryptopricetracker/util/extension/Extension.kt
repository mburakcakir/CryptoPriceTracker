package com.mburakcakir.cryptopricetracker.util

import android.content.Context
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.mburakcakir.cryptopricetracker.ui.entry.CustomTextWatcher

infix fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

infix fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun String.format() = "%$this%"

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : CustomTextWatcher() {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}