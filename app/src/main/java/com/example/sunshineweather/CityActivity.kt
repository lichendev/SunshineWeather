package com.example.sunshineweather

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshineweather.logic.dao.Repository
import kotlinx.android.synthetic.main.activity_city.*
import kotlinx.android.synthetic.main.activity_main.*

class CityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        search_city.setOnClickListener {
            val intent = Intent(this, WeatherActivity::class.java)
            intent.putExtra("city",input_city.text.toString())
            startActivity(intent)
        }
    }
}