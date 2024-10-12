package com.wspyo.ondootdo.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wspyo.ondootdo.entity.TimeEntity
import com.wspyo.ondootdo.repository.TimeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TimesViewModel(application: Application) : AndroidViewModel(application){

    private var _times = MutableLiveData<List<TimeEntity>>()
    val times : LiveData<List<TimeEntity>>
        get() = _times

    private val timeRepository = TimeRepository(application)

    fun getAllTimes() = viewModelScope.launch(Dispatchers.IO) {
//       viewModel에서 .value에 직접적으로 값을 넣을수 없다.
//       _times.value = timeRepository.getAllTimes()
//        _times.postValue(timeRepository.getAllTimes())
        _times.postValue(timeRepository.getAllTimes())
    }

    fun insertTime(time : String) = viewModelScope.launch(Dispatchers.IO) {
        timeRepository.insertTime(time)
        getAllTimes()
    }

    fun deleteAllTimes() = viewModelScope.launch(Dispatchers.IO) {
        timeRepository.deleteAllTimes()
        getAllTimes()
    }

    fun deleteTime(id : Int) = viewModelScope.launch(Dispatchers.IO){
        timeRepository.deleteTime(id)
        getAllTimes()
    }

    fun updateTime(id : Int , time : String) = viewModelScope.launch(Dispatchers.IO) {
        timeRepository.updateTime(id,time)
        getAllTimes()
    }

    fun updateAlarmStatus(id : Int , isEnabled : Boolean) = viewModelScope.launch(Dispatchers.IO){
        timeRepository.updateAlarmStatus(id,isEnabled)
        getAllTimes()
    }
}