package com.wspyo.ondootdo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wspyo.ondootdo.model.local.Document
import com.wspyo.ondootdo.model.local.PlaceResponse
import com.wspyo.ondootdo.repository.LocalRepository
import kotlinx.coroutines.launch
class LocalViewModel(application: Application) : AndroidViewModel(application) {

    private var _placeResponse = MutableLiveData<List<Document>>()
    val placeResponse: LiveData<List<Document>>
        get() = _placeResponse

    private var currentPage: Int = 1
    var isEnd = false
        private set

    private var isLoading = false // 중복 호출 방지
    private val localRepository = LocalRepository()

    fun getPlaceResponse(searchPlace: String) {
        // 이미 마지막 페이지이거나 로딩 중인 경우 요청하지 않음
        if (isEnd || isLoading) return

        isLoading = true
        viewModelScope.launch {
            try {
                val response: PlaceResponse = localRepository.searchPlace(
                    "KakaoAK 90f0265925e2ac638fc4f1d766fd270d",
                    searchPlace,
                    currentPage
                )

                // 새 데이터를 기존 데이터와 합침
                val updatedList = (_placeResponse.value ?: emptyList()) + response.documents
                _placeResponse.value = updatedList

                // 상태 업데이트
                isEnd = response.meta.is_end
                if (!isEnd) currentPage++
            } catch (e: Exception) {
                Log.d("LocalViewModel", "Error: $e")
            } finally {
                isLoading = false // 로딩 상태 해제
            }
        }
    }

    // 초기화 기능 추가 (새 검색 시작 시 호출)
    fun resetSearch() {
        currentPage = 1
        isEnd = false
        _placeResponse.value = emptyList()
    }
}

