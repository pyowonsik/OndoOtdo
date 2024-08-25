package com.wspyo.ondootdo

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.wspyo.ondootdo.viewModel.WeatherViewModel


class MyApplication : Application() {
    val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(WeatherViewModel::class.java)
    }
}