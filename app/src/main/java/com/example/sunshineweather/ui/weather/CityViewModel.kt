package com.example.sunshineweather.ui.weather

import com.example.sunshineweather.logic.dao.Repository

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



}