package com.example.sunshineweather.logic.network

import com.example.sunshineweather.logic.model.CityResponseResult
import com.example.sunshineweather.logic.model.LonLatResponseResult
import com.example.sunshineweather.logic.model.POIResponseResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {
    //restapi.amap.com/v3/geocode/geo?key=您的key&address=北京市&city=北京
    @GET("geocode/geo")
    fun getCityLocation(@Query("key")key:String, @Query("address")address:String,
                    @Query("city")city:String):Call<CityResponseResult>

    //restapi.amap.com/v3/geocode/regeo?output=xml&location=116.310003,39.991957&key=<用户的key>&radius=1000&extensions=all
    @GET("geocode/regeo")
    fun getCityName(@Query("key")key:String,@Query("location")location:String): Call<LonLatResponseResult>

    //restapi.amap.com/v3/place/text?key=您的key&keywords=北京大学&types=高等院校&city=北京&children=1&offset=20&page=1&extensions=all
    @GET("place/text")
    fun getPOI(@Query("key")key:String, @Query("keywords")keywords:String,
                @Query("city")city: String,
               @Query("types")types:String="190100"): Call<POIResponseResult>

}