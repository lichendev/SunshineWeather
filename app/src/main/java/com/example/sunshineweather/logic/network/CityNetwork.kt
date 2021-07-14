package com.example.sunshineweather.logic.network

import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.CLocation
import com.example.sunshineweather.logic.model.CityResponseResult
import com.example.sunshineweather.logic.model.LonLatResponseResult
import com.example.sunshineweather.logic.model.POIResponseResult
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

    fun getCityName(location: CLocation, callbackL: (Call<LonLatResponseResult>, Response<LonLatResponseResult>) -> Unit){
        cityService.getCityName(SunnyWeatherApplication.CityTOKEN,location.toString()).enqueue(
            object : Callback<LonLatResponseResult> {
                override fun onResponse(
                    call: Call<LonLatResponseResult>,
                    response: Response<LonLatResponseResult>
                ) {
                    callbackL(call,response)
                }

                override fun onFailure(call: Call<LonLatResponseResult>, t: Throwable) {
                    t.printStackTrace()
                }
            }
        )
    }

    fun getPOI(keyword:String, city: String,
               callback: (Call<POIResponseResult>, Response<POIResponseResult>) -> Unit){
        cityService.getPOI(SunnyWeatherApplication.CityTOKEN,keyword,city,SunnyWeatherApplication.POI_TYPES).enqueue(
            object : Callback<POIResponseResult>{
                override fun onResponse(
                    call: Call<POIResponseResult>,
                    response: Response<POIResponseResult>
                ) {
                    callback(call,response)
                }

                override fun onFailure(call: Call<POIResponseResult>, t: Throwable) {
                    t.printStackTrace()
                }

            }
        )
    }
}