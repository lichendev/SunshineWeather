package com.example.sunshineweather.logic.dao

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.sunshineweather.R
import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.DailyWeaResResult
import com.example.sunshineweather.logic.model.DailyWeather
import com.example.sunshineweather.logic.model.Location
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.logic.network.CityNetwork
import com.example.sunshineweather.logic.network.WeatherNetwork
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.io.*

object Repository {
    private const val fileName = "real_time_weather.json"
    private val gson = Gson()
    fun refreshRTWeather(longitude: Double, latitude: Double, callback: (RealTimeWeather?)->Unit): RealTimeWeather{
        val rtWeather = loadRTWeatherFromNative()
        //loadRTWeatherFromNetwork
        WeatherNetwork.getRealTimeWeather(longitude, latitude){call, response->
            val requestResult = response.body()
            if (requestResult!=null && requestResult.result!= null && requestResult.result.realtime!=null){
                callback(requestResult.result.realtime)
                saveRTWeatherToNative(requestResult.result.realtime)
            }else{
                callback(null)
            }
        }
        return rtWeather
    }

    fun refreshDailyWeather(longitude: Double, latitude: Double, callback: (List<DailyWeather>?)->Unit){
        WeatherNetwork.getDailyWeather(longitude,latitude){call, response ->
            val result = response.body()
            if(result!=null){
                val list = DailyWeaResResult.convertToDailyWeather(result)
                callback(list)
            }else{
                callback(null)
            }
        }
    }

    fun getCityLocation(city: String, callback: (Location?)->Unit){
        CityNetwork.getCityLocation(city){call,response->
            val cityResponseResult = response.body()
            var location: Location? = null
            if(cityResponseResult!=null && cityResponseResult.geocodes.size > 0)
                location = Location.parseLocation(cityResponseResult.geocodes[0].location)
            callback(location)
        }
    }

    private fun loadRTWeatherFromNative(): RealTimeWeather{
        val context = SunnyWeatherApplication.context
        val file = context.getFileStreamPath(fileName)
        if(file.exists()){
            val fileStream = context.openFileInput(fileName)
            val realTimeWeather = gson.fromJson(BufferedReader(InputStreamReader(fileStream)), RealTimeWeather::class.java)
            if (realTimeWeather!=null)
                return realTimeWeather
        }
        return RealTimeWeather.emptyRTWeather
    }

    private fun saveRTWeatherToNative(rtWeather: RealTimeWeather){
        val rtwJson = gson.toJson(rtWeather)
        val context = SunnyWeatherApplication.context
        val fileOutput = context.openFileOutput(fileName, AppCompatActivity.MODE_PRIVATE)
        val bufferWriter = BufferedWriter(OutputStreamWriter(fileOutput))
        bufferWriter.write(rtwJson)
        bufferWriter.close()
    }

    lateinit var cities:ArrayList<String>
    //var city = SunnyWeatherApplication.context.getString(R.string.default_city)

    fun saveCity(city: String){
        if(!this::cities.isInitialized)
            cities = loadSavedCities()
        if(!cities.contains(city)){
            cities.add(city)
            val edit = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE).edit()
            edit.putString("cities", gson.toJson(cities.toArray()))
            edit.apply()
        }
    }

    fun loadSavedCities(): ArrayList<String>{
        if(!this::cities.isInitialized){
            val csJson = SunnyWeatherApplication.context.getSharedPreferences("main",
                Context.MODE_PRIVATE).getString("cities","")
            //ArrayList<String>::class.java
            cities = ArrayList<String>()
            if(csJson!=null && csJson.isNotEmpty())
                cities.addAll(gson.fromJson(csJson, Array<String>::class.java))
        }
        return cities
    }


}