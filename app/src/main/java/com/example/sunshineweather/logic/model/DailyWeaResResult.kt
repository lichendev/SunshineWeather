package com.example.sunshineweather.logic.model

data class DailyWeaResResult(val status: String, val result: DResult) {
    data class DResult(val daily: Dailys)
    data class Dailys(val status: String, val astro: Array<Astro>, val skycon: Array<Skycon>,
                      val temperature: Array<Temperature>)
    data class Astro(val date:String, val sunrise:SunriseSet, val sunset:SunriseSet)
    data class Skycon(val date: String, val value:String)
    data class Temperature(val date:String, val avg:String, val max:String, val min:String)
    data class SunriseSet(val time: String)

    companion object{
        fun convertToDailyWeather(result: DailyWeaResResult): List<DailyWeather>{
            val list = ArrayList<DailyWeather>()
            val dailys = result.result.daily
            for(i in dailys.astro.indices){
                list.add(DailyWeather(dailys.astro[i].date,dailys.astro[i],dailys.skycon[i],
                dailys.temperature[i]))
            }
            return list
        }
    }
}

data class DailyWeather(val date: String, val astro: DailyWeaResResult.Astro, val skycon: DailyWeaResResult.Skycon,
            val temperature: DailyWeaResResult.Temperature)