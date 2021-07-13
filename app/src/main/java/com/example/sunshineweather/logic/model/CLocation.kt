package com.example.sunshineweather.logic.model

import android.location.Location
import java.lang.Exception

data class CLocation(val longitude: Double, val latitude:Double){

    companion object{

        //str:"116.407526,39.904030"
        fun parseLocation(string: String): CLocation{
            val locs = string.split(",")
            if(locs.size<2)
                throw Exception("location String format is wrong")
            return CLocation(locs[0].toDouble(), locs[1].toDouble())
        }

        fun convertToCLocation(location: Location): CLocation{
            return CLocation(location.longitude,location.latitude)
        }
    }

    override fun toString(): String {
        return "$longitude,$latitude"
    }
}
