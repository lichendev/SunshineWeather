package com.example.sunshineweather.logic.dao

import androidx.appcompat.app.AppCompatActivity
import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.logic.network.WeatherNetwork
import com.google.gson.Gson
import java.io.*

object Repository {
    private const val fileName = "real_time_weather.json"
    private val gson = Gson()
    fun refreshWeather(longitude: Double, latitude: Double, callback: (RealTimeWeather?)->Unit): RealTimeWeather{
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


}