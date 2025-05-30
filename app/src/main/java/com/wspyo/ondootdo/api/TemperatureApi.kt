package com.wspyo.ondootdo.api

import WeatherForecastResponse
import com.wspyo.ondootdo.model.weather.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TemperatureApi {
    @GET("weather")
    suspend fun getCurrentTemperature(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiId: String
    ) : WeatherResponse

    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiId: String
    ) : WeatherForecastResponse
}