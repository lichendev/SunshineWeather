package com.example.sunshineweather

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sunshineweather.logic.model.DailyWeather
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.ui.weather.CityViewModel
import com.example.sunshineweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.daily_weather.*
import kotlinx.android.synthetic.main.header.*
import kotlinx.android.synthetic.main.real_time_weather.*

class WeatherActivity : AppCompatActivity() {

    //硬编码
    private lateinit var city: String

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
            //此时经纬度已经知道，再去请求浪费时间
            refreshData(city)
        }
        add_button.setOnClickListener {
            CityViewModel.saveCity(city)
            Toast.makeText(this,"$city 添加成功", Toast.LENGTH_LONG).show()
        }
        search_button.setOnClickListener {
            //可能导致重复创建多个相同的activity
            val intent = Intent(this, CityActivity::class.java)
            startActivityForResult(intent, WeatherViewModel.CITY_NAME_BACK)
        }
        location_button.setOnClickListener {
            if(checkLocationPermission()){
                WeatherViewModel.refLocalWeather({showCityName(it)},{showRTWeather(it)},{showDailyWeather(it)})
            }
        }

    }


    override fun onResume() {
        super.onResume()
        val ct = intent.getStringExtra("city")
        intent.putExtra("city","")
        when{
            ct?.isNotEmpty() == true ->{
                //清除位置更新监听器
                if(this::city.isInitialized && ct != city)
                    WeatherViewModel.removeLocationUpdates()
                refreshData(ct)
            }
            this::city.isInitialized -> {
                refreshData(city)
            }
            else -> {
                //默认显示定位地址的天气信息
                if(checkLocationPermission()){
                    WeatherViewModel.refLocalWeather({showCityName(it)},{showRTWeather(it)},{showDailyWeather(it)})
                }else{
                    refreshData(WeatherViewModel.getLatestCity())
                }
            }
        }
    }

    override fun onPause() {
        WeatherViewModel.saveLatestCity(city)
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    fun refreshData(city: String){
        this.city = city
        WeatherViewModel.refRTDailyWea(city,
            {rtw->showRTWeather(rtw)},
            {ldw->showDailyWeather(ldw)},
            {showCityName(it)})
    }

    private fun showRTWeather(rtWeather: RealTimeWeather?){
        if(rtWeather!=null){
            tempe_text.text = getString(R.string.temperature_ces,
                                        rtWeather.temperature.toDouble().toInt().toString())
            weath_text.text = WeatherViewModel.convertCodeToChinese(rtWeather.skycon)
            if(rtWeather.air_quality != null)
                aqi_text.text = getString(R.string.aqi,rtWeather.air_quality.aqi.chn)
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
                item.findViewById<TextView>(R.id.dwi_tempe).text = getString(R.string.tem_scope_ces,i.temperature.min,
                                                                            i.temperature.max)
                daily_info.addView(item)
            }
        }
        swipe.isRefreshing = false
    }

    private fun showCityName(city: String?){
        if(city!=null && city.isNotEmpty()) {
            this.city = city
            city_text.text = city
        }else{
            city_text.text = this.city
        }
        Log.d("city","$city")
    }

    //检查定位权限
    private fun checkLocationPermission(): Boolean{
        return if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION),1)
            false
        }else{
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1->{
                if(grantResults.size > 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    WeatherViewModel.refLocalWeather({showCityName(it)},{showRTWeather(it)},{showDailyWeather(it)})
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            WeatherViewModel.CITY_NAME_BACK->{
                if(resultCode == WeatherViewModel.CITY_NAME_BACK && data!=null){
                    intent = data
                }
            }
        }
    }
}