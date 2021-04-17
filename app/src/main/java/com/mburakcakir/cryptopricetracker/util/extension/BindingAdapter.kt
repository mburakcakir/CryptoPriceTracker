package com.mburakcakir.cryptopricetracker.util.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.mburakcakir.cryptopricetracker.R


@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImage(imageUrl: String) {
//    Glide.with(context).load(imageUrl).into(this)
    this.load(imageUrl)
}

@BindingAdapter("backgroundResource")
fun ImageView.setBackground(number: Double) {
    this.setBackgroundResource(if (number > 0) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
}