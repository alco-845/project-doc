package com.alcorp.bovill.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.alcorp.bovill.data.source.local.entity.OrderEntity
import com.alcorp.bovill.data.source.local.room.AppDao

class LocalDataSource private constructor(private val mAppdao: AppDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(appDao: AppDao): LocalDataSource {
            if (INSTANCE == null) {
                INSTANCE = LocalDataSource(appDao)
            }
            return INSTANCE as LocalDataSource
        }
    }

    fun getListOrder(): DataSource.Factory<Int, OrderEntity> = mAppdao.getListOrder()

    fun getOrder(id: Int): LiveData<OrderEntity> = mAppdao.getOrder(id)

    fun insertOrder(booking: List<OrderEntity>) = mAppdao.insertOrder(booking)

    fun updateOrder(booking: List<OrderEntity>) = mAppdao.updateOrder(booking)

    fun deleteOrder(id: Int) = mAppdao.deleteOrder(id)

    fun searchOrder(cari: String): DataSource.Factory<Int, OrderEntity> = mAppdao.searchOrder(cari)

    fun searchTglIn(cari: String, dari: String, ke: String): DataSource.Factory<Int, OrderEntity> = mAppdao.searchTglIn(cari, dari, ke)

    fun searchTglOut(cari: String, dari: String, ke: String): DataSource.Factory<Int, OrderEntity> = mAppdao.searchTglOut(cari, dari, ke)
}