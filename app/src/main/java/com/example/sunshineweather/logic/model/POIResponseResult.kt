package com.example.sunshineweather.logic.model

data class POIResponseResult(val status:String, val info:String, val pois: List<POI>) {
    data class POI(val id:String, val name:String, val location:String, val cityname:String){
        companion object {
            fun convertPOILsTOStrList(pois: List<POI>): List<String> {
                return pois.map {
                    it.name
                }
            }
        }

    }
}