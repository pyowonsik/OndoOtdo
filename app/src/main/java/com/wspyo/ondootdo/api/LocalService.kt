package com.wspyo.ondootdo.api

import com.wspyo.ondootdo.model.local.PlaceResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LocalService {
    @GET("v2/local/search/keyword.json")
    suspend fun searchPlace(
        @Header("Authorization") apiKey: String,  // API 키
        @Query("query") query: String,            // 검색할 장소 이름
//        @Query("page") page: Int = 1              // 검색 결과 페이지 번호 (선택 사항)
    ): PlaceResponse
}



