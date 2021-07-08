package com.example.sunshineweather.logic.network

import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.RequestResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherNetwork {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SunnyWeatherApplication.BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherService = retrofit.create(WeatherService::class.java)

    fun getRealTimeWeather(longitude: Double, latitude: Double, callback:
        (Call<RequestResult>, Response<RequestResult>)->Unit){
        weatherService.getRealTimeWeather(SunnyWeatherApplication.TOKEN, longitude, latitude).enqueue(
            object : Callback<RequestResult>{
                override fun onResponse(
                    call: Call<RequestResult>,
                    response: Response<RequestResult>
                ) {
                    callback(call,response)
                }

                override fun onFailure(call: Call<RequestResult>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }

    fun getRealTimeWeather(longitude: Double, latitude: Double): RequestResult? {
        val response =
            weatherService.getRealTimeWeather(SunnyWeatherApplication.TOKEN, longitude, latitude)
                .execute()
        return response.body()
    }
}