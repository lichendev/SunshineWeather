package com.example.sunshineweather

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.sunshineweather.logic.model.POIResponseResult
import com.example.sunshineweather.ui.weather.CityViewModel
import com.example.sunshineweather.ui.weather.WeatherViewModel
import kotlinx.android.synthetic.main.activity_city.*

class CityActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
//    val textChangedCallback: (Editable?) -> Unit = {
//
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)
        search_city.setOnClickListener {
            val intent = Intent()
            intent.putExtra("city",input_city.text.toString())
            setResult(WeatherViewModel.CITY_NAME_BACK,intent)
            finish()
        }
        adapter = ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,
                                        ArrayList<String>())
        input_city.setAdapter(adapter)
        input_city.addTextChangedListener{text->
            val value = text.toString()
            if(value.length>1){
                CityViewModel.getPOI(value){
                    if(it!=null){
                        //城市查询的逻辑不是很好
                        val newVals = POIResponseResult.POI.convertPOILsTOStrList(it)
                        adapter.clear()
                        adapter.addAll(newVals)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            input_city.refreshAutoCompleteResults()
                        }
                        //Log.d("auto",newVals.toString())
                    }
                }
            }
        }
        city_root.setOnClickListener {
            //当点击根视图时隐藏键盘（如果点击的是EditText，键盘又会立马显示出来；
            // 总体效果是点击EditText外的区域隐藏键盘）
            hideSoftInput(it.windowToken)
        }
    }

//    override fun onDestroy() {
//        //input_city.removeTextChangedListener()
//        super.onDestroy()
//    }

    //隐藏键盘
    private fun hideSoftInput(token: IBinder?){
        if(token!=null){
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                token, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}