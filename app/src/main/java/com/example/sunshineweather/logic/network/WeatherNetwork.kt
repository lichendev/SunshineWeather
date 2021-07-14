package com.example.sunshineweather.logic.network

import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.DailyWeaResResult
import com.example.sunshineweather.logic.model.RTWeaResResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherNetwork {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(SunnyWeatherApplication.WeatherBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherService = retrofit.create(WeatherService::class.java)

    fun getRealTimeWeather(longitude: Double, latitude: Double, callback:
        (Call<RTWeaResResult>, Response<RTWeaResResult>)->Unit){
        weatherService.getRealTimeWeather(SunnyWeatherApplication.WeatherTOKEN, longitude, latitude).enqueue(
            object : Callback<RTWeaResResult>{
                override fun onResponse(
                    call: Call<RTWeaResResult>,
                    response: Response<RTWeaResResult>
                ) {
                    callback(call,response)
                }

                override fun onFailure(call: Call<RTWeaResResult>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }

//    fun getRealTimeWeather(longitude: Double, latitude: Double): RTWeaResResult? {
//        val response =
//            weatherService.getRealTimeWeather(SunnyWeatherApplication.WeatherTOKEN, longitude, latitude)
//                .execute()
//        return response.body()
//    }

    fun getDailyWeather(longitude: Double, latitude: Double, callback:
        (Call<DailyWeaResResult>, Response<DailyWeaResResult>)->Unit){
        weatherService.getDailyWeather(SunnyWeatherApplication.WeatherTOKEN, longitude, latitude).enqueue(
            object : Callback<DailyWeaResResult>{
                override fun onResponse(
                    call: Call<DailyWeaResResult>,
                    response: Response<DailyWeaResResult>
                ) {
                    callback(call,response)
                }

                override fun onFailure(call: Call<DailyWeaResResult>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }
}