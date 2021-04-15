package com.mburakcakir.cryptopricetracker.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import coil.load

infix fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

infix fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImage(imageUrl: String) {
//    Glide.with(context).load(imageUrl).into(this)
    this.load(imageUrl)
}