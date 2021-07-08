package com.example.sunshineweather.logic.network

import com.example.sunshineweather.logic.model.RTWeaResResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {
    @GET("{token}/{longitude},{latitude}/realtime.json")
    fun getRealTimeWeather(@Path("token")token: String, @Path("longitude") longitude: Double,
                           @Path("latitude")latitude:Double): Call<RTWeaResResult>
}