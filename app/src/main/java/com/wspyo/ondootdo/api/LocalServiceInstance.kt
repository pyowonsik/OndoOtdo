package com.wspyo.ondootdo.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LocalServiceInstance {

    private const val KAKAO_BASE_URL = "https://dapi.kakao.com/"

    val client = Retrofit
        .Builder()
        .baseUrl(KAKAO_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getInstance() : Retrofit {
        return client
    }
}
