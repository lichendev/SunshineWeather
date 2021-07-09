package com.example.sunshineweather

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.sunshineweather.logic.dao.Repository
import com.example.sunshineweather.logic.model.DailyWeather
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.daily_weather.*
import kotlinx.android.synthetic.main.real_time_weather.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        var city = intent.getStringExtra("city")
        if(city==null || city.isEmpty())
            city = getString(R.string.default_city)
        WeatherViewModel.refRTDailyWea(city,{rtw->showRTWeather(rtw)},{ldw->showDailyWeather(ldw)})
        swipe.setColorSchemeColors(resources.getColor(R.color.purple_200))
        swipe.setOnRefreshListener {
            WeatherViewModel.refRTDailyWea(city,{rtw->showRTWeather(rtw)},{ldw->showDailyWeather(ldw)})
        }
    }

    private fun showRTWeather(rtWeather: RealTimeWeather?){
        if(rtWeather!=null){
            city_text.text = WeatherViewModel.queryCity
            tempe_text.text = rtWeather.temperature.toDouble().toInt().toString()
            weath_text.text = WeatherViewModel.convertCodeToChinese(rtWeather.skycon)
            if(rtWeather.air_quality!=null)
                aqi_text.text = rtWeather.air_quality.aqi.chn
            real_time_weather.background =
                ContextCompat.getDrawable(this,WeatherViewModel.convertCodeToPhoto(rtWeather.skycon))
        }

    }

    private fun showDailyWeather(listD: List<DailyWeather>?){
        if(listD!=null){
            daily_info.removeAllViews()
            for(i in listD){
                val item = LayoutInflater.from(this).inflate(R.layout.daily_weather_item, daily_info,false)
                item.findViewById<TextView>(R.id.dwi_date).text = i.date.substring(0,10)
                item.findViewById<ImageView>(R.id.dwi_ic).setImageResource(WeatherViewModel.convertCodeToIcon(i.skycon.value))
                item.findViewById<TextView>(R.id.dwi_sky).text = WeatherViewModel.convertCodeToChinese(i.skycon.value)
                item.findViewById<TextView>(R.id.dwi_tempe).text =
                    "${i.temperature.min}~${i.temperature.max}${resources.getString(R.string.celsius)}"
                daily_info.addView(item)
            }
        }
        swipe.isRefreshing = false
    }
}