package com.example.sunshineweather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.sunshineweather.logic.dao.Repository
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.real_time_weather.*

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        var city = intent.getStringExtra("city")
        if(city==null || city.isEmpty())
            city = getString(R.string.default_city)
        WeatherViewModel.refreshWeather(city){
            if(it!=null)
                showRTWeather(it)
        }
    }

    private fun showRTWeather(rtWeather: RealTimeWeather){
        city_text.text = WeatherViewModel.queryCity
        tempe_text.text = rtWeather.temperature.toDouble().toInt().toString()
        weath_text.text = WeatherViewModel.convertCodeToChinese(rtWeather.skycon)
        if(rtWeather.air_quality!=null)
            aqi_text.text = rtWeather.air_quality.aqi.chn
        real_time_weather.background =
            ContextCompat.getDrawable(this,WeatherViewModel.convertCodeToPhoto(rtWeather.skycon))
    }
}