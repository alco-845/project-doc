package com.alcorp.kolamin.di

import com.alcorp.kolamin.data.network.ApiConfig
import com.alcorp.kolamin.data.repository.AppRepository

object Injection {
    fun provideRepository(): AppRepository {
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(apiService)
    }
}