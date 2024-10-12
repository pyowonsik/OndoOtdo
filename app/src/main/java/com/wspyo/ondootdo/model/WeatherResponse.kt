package com.wspyo.ondootdo.model
import com.google.gson.annotations.SerializedName

// retrofit의 반환 값은 model 로 매핑
data class WeatherResponse(
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("rain") val rain: Rain?,  // nullable 처리
    @SerializedName("snow") val snow: Snow?   // nullable 처리
)

data class Rain(
    @SerializedName("1h") val oneHour: Double?,   // 지난 1시간 강수량 (nullable)
    @SerializedName("3h") val threeHour: Double? // 지난 3시간 강수량 (nullable)
)

data class Snow(
    @SerializedName("1h") val oneHour: Double?,   // 지난 1시간 강설량 (nullable)
    @SerializedName("3h") val threeHour: Double? // 지난 3시간 강설량 (nullable)
)

