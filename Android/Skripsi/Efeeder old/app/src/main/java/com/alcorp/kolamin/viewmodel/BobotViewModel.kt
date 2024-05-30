package com.alcorp.kolamin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.alcorp.kolamin.data.repository.AppRepository

class BobotViewModel(private val repository: AppRepository) : ViewModel() {
    fun getDataKapasitas(): LiveData<String> = repository.getDataKapasitas()

    fun getDataBeratTimbangan(): LiveData<String> = repository.getDataBeratTimbangan()

    fun getDataBatasKapasitas(): LiveData<String> = repository.getDataBatasKapasitas()

    fun setDataBatasKapasitas(nilai: Int): LiveData<String> = repository.setDataBatasKapasitas(nilai)
}