package com.wspyo.ondootdo.model

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("sunrise") val sunrise: Int,
    @SerializedName("sunset") val sunset : Int
){
    fun getCurrentSunLight(): Int {
        return (sunset - sunrise) / 3600
    }
}
