package com.alcorp.aryunas.di

import android.content.Context
import com.alcorp.aryunas.data.AppRepository
import com.alcorp.aryunas.data.local.AppDatabase

object Injection {
    fun provideRepository(context: Context) : AppRepository {
        val database = AppDatabase.getInstance(context)
        return AppRepository(database.appDao())
    }
}