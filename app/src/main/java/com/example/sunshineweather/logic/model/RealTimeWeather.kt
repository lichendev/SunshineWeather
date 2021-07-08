package com.example.sunshineweather.logic.model

data class RealTimeWeather(val status: String, val temperature: String, val apparent_temperature: String,
                           val pressure: String, val humidity: String, val wind: Wind, val precipitation: Precipitation,
                            val cloudrate: String, val dswrf: String, val visibility: String, val skycon: String){
    companion object{
        val emptyRTWeather = RealTimeWeather("none","none","none","none","none",
            Wind("none","none"),
            Precipitation(NearestPrecipitation("none","none","none"),
                LocalPrecipitation("none","none","none")),
            "none","none","none","none")
    }
}

data class Wind(val direction: String, val speed: String)

data class Precipitation(val nearest: NearestPrecipitation, val local: LocalPrecipitation)

data class NearestPrecipitation(val status: String, val distance: String, val intensity: String)

data class LocalPrecipitation(val status: String, val intensity: String, val datasource: String)

