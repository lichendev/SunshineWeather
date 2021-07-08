package com.example.sunshineweather.logic.model

data class RTWeaResResult(val status: String, val api_version: String, val api_status:String,
                        val lang: String, val unit:String, val server_time: String, val tzshift: String,
                         val result: WeaResult){
    data class WeaResult(val realtime: RealTimeWeather)
}


