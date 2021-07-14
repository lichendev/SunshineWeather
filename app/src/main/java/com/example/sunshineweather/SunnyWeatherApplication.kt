package com.example.sunshineweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication: Application() {
    companion object{
        //可能造成内存泄漏
        lateinit var context: Context
        const val WeatherTOKEN = "kbJ3ZsRpIog3Z1qd"
        const val WeatherBaseURL = "https://api.caiyunapp.com/v2.5/"
        const val CityTOKEN = "fb7c306e04c85fb52b014368c0b49d0a"
        const val CityBaseURL = "https://restapi.amap.com/v3/"
        const val POI_TYPES = "190100"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}