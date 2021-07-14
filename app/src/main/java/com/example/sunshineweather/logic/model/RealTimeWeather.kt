package com.example.sunshineweather.logic.model

data class RealTimeWeather(val status: String, val temperature: String, val apparent_temperature: String,
                           val pressure: String, val humidity: String, val wind: Wind, val precipitation: Precipitation,
                            val cloudrate: String, val dswrf: String, val visibility: String, val skycon: String,
                            val air_quality: AirQuality,val life_index: LifeIndex){
    companion object{
        val emptyRTWeather = RealTimeWeather("none","none","none","none","none",
            Wind("none","none"),
            Precipitation(NearestPrecipitation("none","none","none"),
                LocalPrecipitation("none","none","none")),
            "none","none","none","none",
            AirQuality("none","none","none","none","none","none",
                AirQuality.AQI("none","none")),
            LifeIndex(LifeIndex.Ultraviolet("none"), LifeIndex.Comfort("none")))
    }

    data class Wind(val direction: String, val speed: String)

    data class Precipitation(val nearest: NearestPrecipitation, val local: LocalPrecipitation)

    data class NearestPrecipitation(val status: String, val distance: String, val intensity: String)

    data class LocalPrecipitation(val status: String, val intensity: String, val datasource: String)

    data class AirQuality(val pm25: String, val pm10: String, val o3: String, val so2:String, val no2:String,
    val co:String, val aqi:AQI){
        data class AQI(val chn:String, val usa:String)
        //data class Description(val usa: String, val chn:String)
    }

    data class LifeIndex(val ultraviolet: Ultraviolet, val comfort: Comfort){
        data class Ultraviolet(val index:String)
        data class Comfort(val index: String)
    }
}
