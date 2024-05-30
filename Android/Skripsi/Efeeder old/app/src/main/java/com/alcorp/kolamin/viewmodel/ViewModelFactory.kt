package com.alcorp.kolamin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alcorp.kolamin.data.repository.AppRepository
import com.alcorp.kolamin.di.Injection

class ViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MonitoringViewModel::class.java)) {
            return MonitoringViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BobotViewModel::class.java)) {
            return BobotViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(EfeedViewModel::class.java)) {
            return EfeedViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.simpleName)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
        }
    }
}