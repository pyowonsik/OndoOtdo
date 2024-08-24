package com.wspyo.ondootdo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wspyo.ondootdo.model.WeatherResponse

import com.wspyo.ondootdo.repository.TemperatureRepository
import kotlinx.coroutines.launch

class TemperatureViewModel(application: Application) : AndroidViewModel(application){

    private var _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse : LiveData<WeatherResponse>
        get() = _weatherResponse

    private var _temperature = MutableLiveData<Double>()
    val temperature : LiveData<Double>
        get() = _temperature

    val temperatureRepository = TemperatureRepository()

    fun getCurrentTemperature(lat : Double,lon : Double , apiId : String) = viewModelScope.launch{
        _weatherResponse.value = temperatureRepository.getCurrentTemperature(lat,lon,apiId)
        _temperature.value = _weatherResponse.value!!.main.getTempInCelsius()
    }

}