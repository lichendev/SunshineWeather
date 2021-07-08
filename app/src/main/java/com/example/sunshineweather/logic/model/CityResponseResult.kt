package com.example.sunshineweather.logic.model

data class CityResponseResult(val status:String, val count:String,val info:String,
                              val geocodes: Array<Geocode>) {
    data class Geocode(val formatted_address: String, val country: String, val province:String, val city:String,
    val citycode:String, val location:String, val level:String)
}