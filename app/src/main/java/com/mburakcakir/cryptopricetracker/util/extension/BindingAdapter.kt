package com.mburakcakir.cryptopricetracker.util.extension

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.mburakcakir.cryptopricetracker.R
import com.mburakcakir.cryptopricetracker.util.afterTextChanged


@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImage(imageUrl: String) {
//    Glide.with(context).load(imageUrl).into(this)
    this.load(imageUrl)
}

@BindingAdapter("setArrowBackground")
fun ImageView.setBackground(number: Double) {
    this.setBackgroundResource(if (number > 0) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
}

@BindingAdapter("afterTextChanged")
fun EditText.afterEditTextChanged(onClick: () -> Unit) {
    this.afterTextChanged {
        onClick.invoke()
    }
    return
}


