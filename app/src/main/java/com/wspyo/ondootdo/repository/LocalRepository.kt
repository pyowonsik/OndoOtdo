package com.wspyo.ondootdo.repository

import com.wspyo.ondootdo.api.LocalService
import com.wspyo.ondootdo.api.LocalServiceInstance
import com.wspyo.ondootdo.api.RetrofitInstance
import com.wspyo.ondootdo.api.TemperatureApi
import retrofit2.http.Header
import retrofit2.http.Query

class LocalRepository {
    val localServiceInstance = LocalServiceInstance.getInstance().create(LocalService::class.java)
    suspend fun searchPlace(apiKey : String , query: String, page : Int) = localServiceInstance.searchPlace(apiKey,query,page)
}
