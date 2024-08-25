package com.wspyo.ondootdo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wspyo.ondootdo.databinding.ActivityMainBinding
import com.wspyo.ondootdo.viewModel.WeatherViewModel
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        weatherViewModel = (application as MyApplication).weatherViewModel

//        Log.d("ViewModel Test : MainActivity",weatherViewModel.weatherResponse.value.toString())

        // 현재 시간을 얻기
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)

//            Log.d("MainActivity", "{$hour : $minute : $second}" )


    }
}
