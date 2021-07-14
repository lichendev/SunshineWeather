package com.example.sunshineweather

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshineweather.ui.place.CityAdapter
import com.example.sunshineweather.ui.place.CityItemTouchCallback
import com.example.sunshineweather.ui.weather.WeatherViewModel


class CityFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.city_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //val test1 = Repository.loadSavedCities()
        val adapter = CityAdapter(requireContext()){
            activity?.let{act->
                if(activity is WeatherActivity){
                    val wActivity = activity as WeatherActivity
                    wActivity.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
                    wActivity.refreshData(it.text.toString())
                }else{
                    val intent = Intent()
                    intent.putExtra("city",it.text.toString())
                    act.setResult(WeatherViewModel.CITY_NAME_BACK, intent)
                    act.finish()
                }
            }
        }
        recyclerView.adapter = adapter
        ItemTouchHelper(CityItemTouchCallback(adapter)).attachToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration(){
            override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                outRect.bottom = 32
            }
        })
        return view
    }
}