package com.prac.cabstone

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    val TAG = "CabStone"
    val JWT = "X_ACCESS_TOKEN"
    val LANGUAGE = "LANGUAGE"
    val prefs: SharedPreferences = context.getSharedPreferences(TAG, 0)

    var myJWT: String?
        get() = prefs.getString(JWT, "")
        set(value) = prefs.edit().putString(JWT, value).apply()

    var myLANGUAGE: String?
        get() = prefs.getString(LANGUAGE, "ko")
        set(value) = prefs.edit().putString(LANGUAGE, value).apply()
}