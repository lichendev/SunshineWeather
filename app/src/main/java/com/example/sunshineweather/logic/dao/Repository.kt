package com.example.sunshineweather.logic.dao

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.sunshineweather.R
import com.example.sunshineweather.SunnyWeatherApplication
import com.example.sunshineweather.logic.model.*
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
        WeatherNetwork.getRealTimeWeather(longitude, latitude){_, response->
            val requestResult = response.body()
            if (requestResult?.result?.realtime != null){
//            if (requestResult!=null && requestResult.result!= null && requestResult.result.realtime!=null){
                callback(requestResult.result.realtime)
                saveRTWeatherToNative(requestResult.result.realtime)
            }else{
                callback(null)
            }
        }
        return rtWeather
    }

    fun refreshDailyWeather(longitude: Double, latitude: Double, callback: (List<DailyWeather>?)->Unit){
        WeatherNetwork.getDailyWeather(longitude,latitude){_, response ->
            val result = response.body()
            if(result!=null){
                val list = DailyWeaResResult.convertToDailyWeather(result)
                callback(list)
            }else{
                callback(null)
            }
        }
    }

    fun getCityLocation(city: String, callbackL: (CLocation?)->Unit, callbackCity: (String?) -> Unit){
        CityNetwork.getCityLocation(city){_,response->
            val cityResponseResult = response.body()
            var location: CLocation? = null
            var formatCity: String? = null
            if(cityResponseResult!=null && cityResponseResult.geocodes.isNotEmpty()) {
                location = CLocation.parseLocation(cityResponseResult.geocodes[0].location)
                formatCity = cityResponseResult.geocodes[0].formatted_address
            }
            callbackL(location)
            callbackCity(formatCity)
        }
    }

    fun getCityName(location: CLocation, callback: (String?)->Unit){
        CityNetwork.getCityName(location){_,response->
            val lonLatResponseResult = response.body()
            var city: String? = null
            if(lonLatResponseResult!=null) {
                city = lonLatResponseResult.regeocode.formatted_address
            }
            callback(city)
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

    private lateinit var cities: LinkedList<String>
    private lateinit var sharedPreferences: SharedPreferences
    //var city = SunnyWeatherApplication.context.getString(R.string.default_city)

    fun saveCity(city: String?){
        if(!this::cities.isInitialized)
            cities = loadSavedCities()
        if(city!=null && !cities.contains(city)){
            cities.add(city)
        }
        if(!this::sharedPreferences.isInitialized)
            sharedPreferences = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.putString("cities", gson.toJson(cities.toArray()))
        edit.apply()
    }

    fun loadSavedCities(): LinkedList<String>{
        if(!this::sharedPreferences.isInitialized)
            sharedPreferences = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE)
        if(!this::cities.isInitialized){
            val csJson = sharedPreferences.getString("cities","")
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

    private lateinit var locationManager: LocationManager
    private lateinit var locationProvider:String
    private var locationUpdateCallback: ((Location)->Unit) ?= null
     fun getUpdateLocation(context: Context, callback: (Location)->Unit) {
         if(!this::locationManager.isInitialized){
             //1.获取位置管理器
             locationManager =
                 context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
             //2.获取位置提供器，GPS或是NetWork
             val providers: List<String> = locationManager.getProviders(true)
             locationProvider = when {
                 providers.contains(LocationManager.NETWORK_PROVIDER) -> {
                     //如果是网络定位
                     LocationManager.NETWORK_PROVIDER
                 }
                 providers.contains(LocationManager.GPS_PROVIDER) -> {
                     //如果是GPS定位
                     LocationManager.GPS_PROVIDER
                 }
                 else -> {
                     Toast.makeText(context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show()
                     return
                 }
             }
         }

        //3.获取上次的位置，一般第一次运行，此值为null
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            removeLocationUpdates()
            locationUpdateCallback={
                callback(it)
            }
            locationUpdateCallback?.let {
                locationManager.requestLocationUpdates(locationProvider, 1000000, 1000f,
                   it
                )
            }
        }
    }

    fun removeLocationUpdates(){
        locationUpdateCallback?.let {
            locationManager.removeUpdates(it)
        }
        locationUpdateCallback = null
    }

    fun getPOI(keyword:String, city: String?,
               callback: (List<POIResponseResult.POI>?) -> Unit){
        if(city==null){
            CityNetwork.getPOI(keyword,""){ _, response ->
                val pois = response.body()?.pois
                callback(pois)
            }
        }else{
            CityNetwork.getPOI(keyword,city){_, response ->
                val pois = response.body()?.pois
                callback(pois)
            }
        }

    }

    /**
     * 获取WeatherActivity最近显示的city
     */
    fun getLatestCity(): String{
        if(!this::sharedPreferences.isInitialized)
            sharedPreferences = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("latestCity",null)
        return value?:SunnyWeatherApplication.context.getString(R.string.default_city)
    }

    /**
     * 存储WeatherActivity最近显示的city
     */
    fun saveLatestCity(city: String){
        if(!this::sharedPreferences.isInitialized)
            sharedPreferences = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE)
        if (city.isNotEmpty()){
            val edit = sharedPreferences.edit()
            edit.putString("latestCity", city)
            edit.apply()
        }
    }

    fun getLocalCity():String{
        if(!this::sharedPreferences.isInitialized)
            sharedPreferences = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString("localCity",null)
        return value?:SunnyWeatherApplication.context.getString(R.string.default_city)
    }

    fun saveLocalCity(city:String){
        if(!this::sharedPreferences.isInitialized)
            sharedPreferences = SunnyWeatherApplication.context.getSharedPreferences("main", Context.MODE_PRIVATE)
        if (city.isNotEmpty()){
            val edit = sharedPreferences.edit()
            edit.putString("localCity", city)
            edit.apply()
        }
    }
}