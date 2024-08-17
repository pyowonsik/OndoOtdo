package com.wspyo.ondootdo.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class temperatureViewModel : ViewModel() {

    private var _temperature = MutableLiveData<Int>(0)
    val temperature : LiveData<Int>
        get() = _temperature





}