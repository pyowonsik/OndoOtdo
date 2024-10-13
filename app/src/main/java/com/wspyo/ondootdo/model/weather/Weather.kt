package com.wspyo.ondootdo.model.weather

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description : String
){
    fun getCurrentWeather(): String {
        return main.toString()
    }
}
