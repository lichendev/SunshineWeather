package com.example.sunshineweather.ui.weather

import android.util.Log
import com.example.sunshineweather.R
import com.example.sunshineweather.logic.dao.Repository
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.logic.network.CityNetwork

object WeatherViewModel {

    //硬编码，不太好
    val mapCodeToChinese = mapOf<String,String>("CLEAR_DAY" to "晴（白天）", "CLEAR_NIGHT" to "晴（夜间）",
        "PARTLY_CLOUDY_DAY" to "多云（白天）","PARTLY_CLOUDY_NIGHT" to "多云（夜间）","CLOUDY" to "阴",
        "LIGHT_HAZE" to "轻度雾霾",
        "MODERATE_HAZE" to "中度雾霾","HEAVY_HAZE" to "重度雾霾","LIGHT_RAIN" to "小雨","MODERATE_RAIN" to "中雨",
        "HEAVY_RAIN" to "大雨","STORM_RAIN" to "暴雨","FOG" to "雾","LIGHT_SNOW" to "小雪",
        "MODERATE_SNOW" to "中雪","HEAVY_SNOW" to "大雪","STORM_SNOW" to "暴雪","DUST" to "浮尘",
        "SAND" to "沙尘", "WIND" to "大风")

    fun convertCodeToChinese(code: String): String{
        if (mapCodeToChinese.containsKey(code))
            return mapCodeToChinese[code]!!
        else
            return "none"
    }

    fun convertCodeToPhoto(code: String): Int{
        return when(code){
            "CLEAR_DAY"-> R.drawable.bg_clear_day
            "CLEAR_NIGHT"-> R.drawable.bg_clear_night
            "PARTLY_CLOUDY_DAY"->R.drawable.bg_partly_cloudy_day
            "PARTLY_CLOUDY_NIGHT"->R.drawable.bg_partly_cloudy_night
            "CLOUDY"->R.drawable.bg_cloudy
            "FOG"->R.drawable.bg_fog
            "LIGHT_RAIN","MODERATE_RAIN","HEAVY_RAIN","STORM_RAIN"->R.drawable.bg_rain
            "LIGHT_SNOW","MODERATE_SNOW","HEAVY_SNOW","STORM_SNOW"->R.drawable.bg_snow
            "WIND"->R.drawable.bg_wind
            else->R.drawable.bg_clear_day
        }
    }

    fun refreshWeather(city:String, callback: (RealTimeWeather?)->Unit){
        Repository.getCityLocation(city) {
            queryCity = city
            if(it!=null){
                 Repository.refreshWeather(it.longitude,it.latitude,callback)
            }else{
                callback(null)
            }
        }
    }

    var queryCity:String = "抚州"
}