package com.example.sunshineweather.logic.dao

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.DailyWeaResResult
import com.example.sunshineweather.logic.model.DailyWeather
import com.example.sunshineweather.logic.model.CLocation
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.logic.network.CityNetwork
import com.example.sunshineweather.logic.network.WeatherNetwork
import com.google.gson.Gson
import java.io.*
import java.util.*

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

    fun getCityLocation(city: String, callback: (CLocation?)->Unit){
        CityNetwork.getCityLocation(city){call,response->
            val cityResponseResult = response.body()
            var location: CLocation? = null
            if(cityResponseResult!=null && cityResponseResult.geocodes.size > 0)
                location = CLocation.parseLocation(cityResponseResult.geocodes[0].location)
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

    lateinit var cities: LinkedList<String>
    //var city = SunnyWeatherApplication.context.getString(R.string.default_city)

    fun saveCity(city: String?){
        if(!this::cities.isInitialized)
            cities = loadSavedCities()
        if(city!=null && !cities.contains(city)){
            cities.add(city)
        }
        val edit = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE).edit()
        edit.putString("cities", gson.toJson(cities.toArray()))
        edit.apply()
    }

    fun loadSavedCities(): LinkedList<String>{
        if(!this::cities.isInitialized){
            val csJson = SunnyWeatherApplication.context.getSharedPreferences("main",
                Context.MODE_PRIVATE).getString("cities","")
            //ArrayList<String>::class.java
            cities = LinkedList<String>()
            if(csJson!=null && csJson.isNotEmpty())
                cities.addAll(gson.fromJson(csJson, Array<String>::class.java))
        }
        return cities
    }

    fun swapPositionCity(source: Int, target:Int): List<String>{
        Collections.swap(cities,source,target)
        saveCity(null)
        return cities
    }

    fun removeCity(position: Int): List<String>{
        cities.removeAt(position)
        saveCity(null)
        return cities
    }

    private fun getUpdateLocation(context: Context, callback: (android.location.Location)->Unit) {
        //1.获取位置管理器
        val locationManager =
            context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
        //2.获取位置提供器，GPS或是NetWork
        val providers: List<String> = locationManager.getProviders(true)
        val locationProvider: String
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER
        } else {
            Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show()
            return
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(locationProvider, 0, 0f) {
                callback(it)
            }
        }
    }

}