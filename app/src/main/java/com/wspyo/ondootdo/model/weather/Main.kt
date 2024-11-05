package com.wspyo.ondootdo.model.weather

import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class Main(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val grndLevel: Int
) {
    private fun roundToFirstDecimal(value: Double): Double {
        return (value * 10.0).roundToInt() / 10.0
    }
    fun getTempInCelsius(): Int {
        return Math.round(roundToFirstDecimal(temp - 273.15)).toInt()
    }
    fun getFeelsLikeInCelsius(): Int {
        return Math.round(roundToFirstDecimal(feelsLike - 273.15)).toInt()
    }
}