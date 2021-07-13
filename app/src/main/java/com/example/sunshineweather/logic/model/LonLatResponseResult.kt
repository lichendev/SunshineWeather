package com.example.sunshineweather.logic.model

data class LonLatResponseResult (val status: String,val regeocode: Regeocode){
    data class Regeocode(val formatted_address: String)
}