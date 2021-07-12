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
        setSupportActionBar(main_toolbar)
        query_button.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("city",city_edit.text.toString())
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toorbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.add_menu->Toast.makeText(this,"add",Toast.LENGTH_LONG).show()
            R.id.search_menu-> {
                //可能导致重复创建多个相同的activity
                val intent = Intent(this, CityActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}