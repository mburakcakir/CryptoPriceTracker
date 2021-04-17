package com.mburakcakir.cryptopricetracker.util


fun String.formatUpdatedTime(): String {
    val year = substring(0, 4)
    val month = substring(5, 7)
    val day = substring(8, 10)
    val hour = substring(11, 13)
    val minute = substring(14, 16)
    val second = substring(17, 19)
    return "$day-$month-$year, ${Integer.parseInt(hour) + 3}:$minute:$second"
}

fun Number.formatPriceChange(): Double {
    return String.format("%.2f", this).replace(",", ".").toDouble()
}

fun setFavouriteMessage(isFavourite: Boolean): String {
    return if (isFavourite) "Added" else "Removed"
}