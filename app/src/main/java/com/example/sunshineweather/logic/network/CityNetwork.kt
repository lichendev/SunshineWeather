package com.example.sunshineweather.logic.network

import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.CityResponseResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CityNetwork {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SunnyWeatherApplication.CityBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val cityService = retrofit.create(CityService::class.java)

    fun getCityLocation(city: String, callback:
        (Call<CityResponseResult>, Response<CityResponseResult>)->Unit){
        cityService.getCityLocation(SunnyWeatherApplication.CityTOKEN,city,city).enqueue(
            object : Callback<CityResponseResult> {
                override fun onResponse(
                    call: Call<CityResponseResult>,
                    response: Response<CityResponseResult>
                ) {
                    callback(call,response)
                }

                override fun onFailure(call: Call<CityResponseResult>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }
}