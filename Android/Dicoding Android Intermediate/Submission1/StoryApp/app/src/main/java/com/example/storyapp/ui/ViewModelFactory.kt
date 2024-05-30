package com.example.storyapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.AppRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.ui.add.AddViewModel
import com.example.storyapp.ui.detail.DetailViewModel
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.main.MainViewModel
import com.example.storyapp.ui.regis.RegisViewModel

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(RegisViewModel::class.java)) {
            return RegisViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(repository) as T
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