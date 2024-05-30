package com.alcorp.aryunas.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alcorp.aryunas.data.AppRepository
import com.alcorp.aryunas.di.Injection
import com.alcorp.aryunas.ui.kamar.KamarViewModel
import com.alcorp.aryunas.ui.order.OrderViewModel

class ViewModelFactory (private val repository: AppRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KamarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KamarViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}