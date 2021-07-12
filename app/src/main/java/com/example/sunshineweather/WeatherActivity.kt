package com.example.sunshineweather

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sunshineweather.logic.dao.Repository
import com.example.sunshineweather.logic.model.DailyWeather
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.daily_weather.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.real_time_weather.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    //硬编码
    var city = "南昌"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)
        swipe.setColorSchemeColors(resources.getColor(R.color.purple_200))
        swipe.setOnRefreshListener {
            WeatherViewModel.refRTDailyWea(city,{rtw->showRTWeather(rtw)},{ldw->showDailyWeather(ldw)})
        }
        add_button.setOnClickListener {
            Repository.saveCity(city)
            Toast.makeText(this,"$city 添加成功", Toast.LENGTH_LONG).show()
        }
        search_button.setOnClickListener {
            //可能导致重复创建多个相同的activity
            val intent = Intent(this, CityActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val ct = intent.getStringExtra("city")
        if(ct!=null)
            city = ct
        refreshData(city)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    fun refreshData(city: String){
        this.city = city
        WeatherViewModel.refRTDailyWea(city,{rtw->showRTWeather(rtw)},{ldw->showDailyWeather(ldw)})
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