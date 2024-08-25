package com.wspyo.ondootdo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wspyo.ondootdo.databinding.ActivityMainBinding
import com.wspyo.ondootdo.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        weatherViewModel = (application as MyApplication).weatherViewModel

//        Log.d("ViewModel Test : MainActivity",weatherViewModel.weatherResponse.value.toString())
    }
}
