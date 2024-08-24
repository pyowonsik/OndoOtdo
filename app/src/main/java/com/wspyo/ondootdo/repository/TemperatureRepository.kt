package com.wspyo.ondootdo.repository

import com.wspyo.ondootdo.api.RetrofitInstance
import com.wspyo.ondootdo.api.TemperatureApi

class TemperatureRepository {
    val retrofitInstance = RetrofitInstance.getInstance().create(TemperatureApi::class.java)
    suspend fun getCurrentTemperature(lat : Double , lon : Double , apiId : String ) = retrofitInstance.getCurrentTemperature(lat,lon,apiId)
}