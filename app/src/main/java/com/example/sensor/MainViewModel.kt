package com.example.sensor

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    val doListen = MutableLiveData(false)

    val gravity = arrayListOf(0.0, 0.0, 0.0)

    val linearAcceleration = arrayListOf(0.0, 0.0, 0.0)

    val liveLinearAcceleration = MutableLiveData(arrayListOf(0.0, 0.0, 0.0))

}