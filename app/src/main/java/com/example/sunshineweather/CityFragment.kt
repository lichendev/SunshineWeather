package com.example.sunshineweather

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sunshineweather.logic.dao.Repository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CityFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_city, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.city_recycle_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        //val test1 = Repository.loadSavedCities()
        recyclerView.adapter = CityAdapter(requireContext(), Repository.loadSavedCities()){
            if(activity!=null){
                if(activity is WeatherActivity){
                    val wActivity = activity as WeatherActivity
                    wActivity.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
                    wActivity.refreshData(it.text.toString())
                }else{
                    val intent = Intent(SunnyWeatherApplication.context,WeatherActivity::class.java)
                    intent.putExtra("city",it.text.toString())
                    (activity as FragmentActivity).startActivity(intent)
                }
            }

        }
        return view
    }

    class CityAdapter(val context: Context, val data: List<String>,
        val callback: (TextView)->Unit) : RecyclerView.Adapter<CityAdapter.ViewHolder>(){
        class ViewHolder(view: View, val callback: (TextView)->Unit) : RecyclerView.ViewHolder(view){
            val city_text = view.findViewById<TextView>(R.id.city_recycle_item)
            init {
                city_text.setOnClickListener {
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
            holder.city_text.text = data[position]
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
}