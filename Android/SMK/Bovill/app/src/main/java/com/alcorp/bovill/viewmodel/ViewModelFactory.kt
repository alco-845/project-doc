package com.alcorp.bovill.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alcorp.bovill.data.source.AppRepository
import com.alcorp.bovill.di.Injection
import com.alcorp.bovill.ui.addEditBooking.AddEditBookingViewModel
import com.alcorp.bovill.ui.order.OrderViewModel

class ViewModelFactory private constructor(private val mAppRepository: AppRepository) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) : ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context))
                }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(OrderViewModel::class.java) -> {
                return OrderViewModel(mAppRepository) as T
            }

            modelClass.isAssignableFrom(AddEditBookingViewModel::class.java) -> {
                return AddEditBookingViewModel(mAppRepository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}