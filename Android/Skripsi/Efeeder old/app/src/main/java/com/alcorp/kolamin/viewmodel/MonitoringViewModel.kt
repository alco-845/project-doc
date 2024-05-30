package com.alcorp.kolamin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alcorp.kolamin.data.repository.AppRepository

class MonitoringViewModel(private val repository: AppRepository) : ViewModel() {
    fun getDataBatasSuhu(): LiveData<String> = repository.getDataBatasSuhu()

    fun setDataBatasSuhu(nilai: Int): LiveData<String> = repository.setDataBatasSuhu(nilai)

    fun getDataSuhu(): LiveData<String> = repository.getDataSuhu()

    fun getDataOxygen(): LiveData<String> = repository.getDataOxygen()

    fun getDataPh(): LiveData<String> = repository.getDataPh()

    fun getDataSwitch(): LiveData<Int> = repository.SwitchRepository().getDataSwitch()

    fun setDataSwitch(nilai: Int) = repository.SwitchRepository().setDataSwitch(nilai)
}