package com.example.sunshineweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication: Application() {
    companion object{
        lateinit var context: Context
        const val TOKEN = "kbJ3ZsRpIog3Z1qd"
        const val BASEURL = "https://api.caiyunapp.com/v2.5/"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}