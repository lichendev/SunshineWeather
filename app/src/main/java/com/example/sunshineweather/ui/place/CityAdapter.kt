package com.example.sunshineweather.ui.place

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshineweather.R
import com.example.sunshineweather.ui.weather.CityViewModel

class CityAdapter(val context: Context,
                  private val callback: (TextView)->Unit) : RecyclerView.Adapter<CityAdapter.ViewHolder>(),
    ItemMoveSwipeCallback {

    private var data: List<String> = CityViewModel.getCities()

    class ViewHolder(view: View, val callback: (TextView)->Unit) : RecyclerView.ViewHolder(view){
        val cityText: TextView = view.findViewById(R.id.city_recycle_item)
        init {
            cityText.setOnClickListener {
                val textV = it as TextView
                callback(textV)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_city_item,parent,false)
        return ViewHolder(view,callback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cityText.text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onItemMove(sourcePosition: Int, targetPosition: Int): Boolean {
        data = CityViewModel.swapPositionCity(sourcePosition,targetPosition)
        notifyItemMoved(sourcePosition,targetPosition)
        return true
    }

    override fun onItemSwipe(sourcePosition: Int) {
        data = CityViewModel.removeCity(sourcePosition)
        notifyItemRemoved(sourcePosition)
    }
}