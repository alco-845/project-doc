package com.alcorp.bovill.data.source

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.alcorp.bovill.data.source.local.entity.OrderEntity

interface AppDataSource {

    fun getListOrder(): LiveData<PagedList<OrderEntity>>

    fun getOrder(id: Int): LiveData<OrderEntity>

    fun insertOrder(booking: OrderEntity)

    fun updateOrder(booking: OrderEntity)

    fun deleteOrder(id: Int)

    fun searchOrder(cari: String): LiveData<PagedList<OrderEntity>>

    fun searchTglIn(cari: String, dari: String, ke: String): LiveData<PagedList<OrderEntity>>

    fun searchTglOut(cari: String, dari: String, ke: String): LiveData<PagedList<OrderEntity>>
}