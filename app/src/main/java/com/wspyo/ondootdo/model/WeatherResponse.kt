package com.wspyo.ondootdo.model
import com.google.gson.annotations.SerializedName

// retrofit의 반환 값은 model 로 매핑
data class WeatherResponse(
    @SerializedName("main") val main: Main
)
