package com.example.storyapp.di

import com.example.storyapp.data.AppRepository
import com.example.storyapp.data.remote.retrofit.ApiConfig


object Injection {
    fun provideRepository(): AppRepository {
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(apiService)
    }
}