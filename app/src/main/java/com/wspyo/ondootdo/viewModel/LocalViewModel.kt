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
    private var _placeResponse = MutableLiveData<PlaceResponse>()
    val placeResponse : LiveData<PlaceResponse>
        get() = _placeResponse



    val localRepository  = LocalRepository()

    fun getPlaceResponse(searchPlace : String ) = viewModelScope.launch {
//        localRepository.searchPlace("KakaoAK 90f0265925e2ac638fc4f1d766fd270d","까치산역 스타벅스",1)
//        Log.d("LocalViewModel", localRepository.searchPlace("KakaoAK 90f0265925e2ac638fc4f1d766fd270d","까치산역 스타벅스",1)
//            .toString())
        _placeResponse.value = localRepository.searchPlace("KakaoAK 90f0265925e2ac638fc4f1d766fd270d",searchPlace)
//        Log.d("LocalViewModel",_placeResponse.value!!.documents.filter { it.place_name == "강남구청" }.toString())
    }
}

