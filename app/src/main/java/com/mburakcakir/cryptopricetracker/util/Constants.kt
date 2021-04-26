package com.mburakcakir.cryptopricetracker.util

object Constants {

    const val INVALID_PASSWORD =
        "Password must contain uppercase letters, lowercase letters, numbers and must be at least 6 digits."
    const val INVALID_EMAIL = "Email format does not match."

    const val DB_NAME = "crypto.db"
    const val BASE_COLLECTION_NAME = "Cryptocurrency"
    const val DETAIL_COLLECTION_NAME = "listFavouriteCrypto"
    const val PREF_NAME: String = "CryptoPriceTracker"
    const val REFRESH_INTERVAL: String = "REFRESH_INTERVAL"
    const val PASSWORD_PATTERN: String = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$"

}