package com.example.sunshineweather.logic.network

import com.example.sunshineweather.logic.model.CityResponseResult
import com.example.sunshineweather.logic.model.LonLatResponseResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {
    //restapi.amap.com/v3/geocode/geo?key=您的key&address=北京市&city=北京
    @GET("geo")
    fun getCityLocation(@Query("key")key:String, @Query("address")address:String,
                    @Query("city")city:String):Call<CityResponseResult>
    //restapi.amap.com/v3/geocode/regeo?output=xml&location=116.310003,39.991957&key=<用户的key>&radius=1000&extensions=all
    @GET("regeo")
    fun getCityName(@Query("key")key:String,@Query("location")location:String): Call<LonLatResponseResult>

}