package com.prac.cabstone

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class ApplicationClass : Application() {
    override fun onCreate() {
        prefs = AppPreferences(applicationContext)
        super.onCreate()
    }

    companion object {

        lateinit var prefs : AppPreferences
        // 실서버 주소
        var BASE_URL = "http://49.50.175.133:3000"
    }
}