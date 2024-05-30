package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.AppRepository
import com.example.storyapp.data.local.room.AppDatabase
import com.example.storyapp.data.remote.retrofit.ApiConfig


object Injection {
    fun provideRepository(context: Context): AppRepository {
        val database = AppDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(database, apiService)
    }
}