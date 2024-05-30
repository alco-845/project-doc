package com.alcorp.bovill.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.alcorp.bovill.data.source.AppRepository
import com.alcorp.bovill.data.source.local.entity.OrderEntity

class OrderViewModel(private val appRepository: AppRepository) : ViewModel() {

    var order: LiveData<PagedList<OrderEntity>>? = null
        get() {
            if (field == null) {
                field = getListOrder()
            }
            return field
        }
        private set

    fun getListOrder() : LiveData<PagedList<OrderEntity>> = appRepository.getListOrder()

    fun removeOrder(id: Int) = appRepository.deleteOrder(id)

    fun searchOrder(cari: String) : LiveData<PagedList<OrderEntity>> = appRepository.searchOrder(cari)

    fun searchTglIn(cari: String, dari: String, ke: String) : LiveData<PagedList<OrderEntity>> = appRepository.searchTglIn(cari, dari, ke)

    fun searchTglOut(cari: String, dari: String, ke: String) : LiveData<PagedList<OrderEntity>> = appRepository.searchTglOut(cari, dari, ke)
}