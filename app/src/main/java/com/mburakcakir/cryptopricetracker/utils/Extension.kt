package com.mburakcakir.cryptopricetracker.utils

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

infix fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImage(imageUrl: String) {
    Glide.with(context).load(imageUrl).into(this)
//    this.load(imageUrl)
}