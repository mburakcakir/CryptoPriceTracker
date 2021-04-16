package com.mburakcakir.cryptopricetracker.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import coil.load
import com.mburakcakir.cryptopricetracker.R

infix fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

infix fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun Drawable.setFilter(color: Int) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//        colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP);
//    } else {
//        setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//    }
}

@BindingAdapter("loadImageFromUrl")
fun ImageView.loadImage(imageUrl: String) {
//    Glide.with(context).load(imageUrl).into(this)
    this.load(imageUrl)
}

@BindingAdapter("backgroundResource")
fun ImageView.setBackground(number: Double) {
    this.setBackgroundResource(if (number > 0) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down)
}