package com.wspyo.ondootdo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wspyo.ondootdo.model.local.PlaceResponse
import com.wspyo.ondootdo.model.weather.WeatherResponse
import com.wspyo.ondootdo.repository.LocalRepository
import com.wspyo.ondootdo.repository.TemperatureRepository
import kotlinx.coroutines.launch

class PlaceDetailsViewModel(application: Application) : AndroidViewModel(application)  {

    private var _weatherRepsonse = MutableLiveData<WeatherResponse>()
    val weatherResponse : LiveData<WeatherResponse>
        get() = _weatherRepsonse

    val repository = TemperatureRepository()

    fun getPlaceDetailWeather(lat: Double,lon: Double,apiId: String) = viewModelScope.launch {
        _weatherRepsonse.value = repository.getCurrentTemperature(lat,lon,apiId)
    }

}