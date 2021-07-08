package com.example.sunshineweather

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.sunshineweather.logic.dao.Repository
import com.example.sunshineweather.logic.model.RealTimeWeather
import com.example.sunshineweather.logic.model.RequestResult
import com.example.sunshineweather.logic.network.WeatherService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refresh_button.setOnClickListener {
            val rtw = Repository.refreshWeather(121.622816,31.355535){
                if(it!=null)
                    showRTWeather(it)
            }
            showRTWeather(rtw)
        }
    }

    fun showRTWeather(rtWeather: RealTimeWeather){
        text_view.text = rtWeather.skycon
    }

}