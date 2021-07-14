package com.example.sunshineweather.ui.weather

import com.example.sunshineweather.logic.dao.Repository
import com.example.sunshineweather.logic.model.POIResponseResult

object CityViewModel {
    fun getCities():List<String>{
        return Repository.loadSavedCities()
    }

    fun saveCity(city: String?){
        Repository.saveCity(city)
    }

    fun swapPositionCity(source: Int, target:Int): List<String>{
        return Repository.swapPositionCity(source,target)
    }

    fun removeCity(position: Int): List<String>{
        return Repository.removeCity(position)
    }

    fun getPOI(keyword:String, callback: (List<POIResponseResult.POI>?)->Unit){
        Repository.getPOI(keyword, Repository.getLocalCity(),callback)
    }

}