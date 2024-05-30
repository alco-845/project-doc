package com.alcorp.bovill.data.source

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alcorp.bovill.data.source.local.LocalDataSource
import com.alcorp.bovill.data.source.local.entity.OrderEntity
import com.alcorp.bovill.utils.AppExecutors

class AppRepository private constructor(
        private val localDataSource: LocalDataSource,
        private val appExecutors: AppExecutors
) : AppDataSource {

    companion object {
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(localDataSource: LocalDataSource, appExecutors: AppExecutors): AppRepository =
                instance ?: synchronized(this) {
                    instance ?: AppRepository(localDataSource, appExecutors)
                }
    }

    override fun getListOrder(): LiveData<PagedList<OrderEntity>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build()
        return LivePagedListBuilder(localDataSource.getListOrder(), config).build()
    }

    override fun getOrder(id: Int): LiveData<OrderEntity> {
        return localDataSource.getOrder(id)
    }

    override fun insertOrder(booking: OrderEntity) {
        val list = ArrayList<OrderEntity>()
        val entity = OrderEntity(
            booking.id_order,
            booking.nama,
            booking.tglorder,
            booking.subtotal,
            booking.diskon,
            booking.bayar
        )
        list.add(entity)
        appExecutors.diskIO().execute { localDataSource.insertOrder(list) }
    }

    override fun updateOrder(booking: OrderEntity) {
        val list = ArrayList<OrderEntity>()
        val entity = OrderEntity(
            booking.id_order,
            booking.nama,
            booking.tglorder,
            booking.subtotal,
            booking.diskon,
            booking.bayar
        )
        list.add(entity)
        appExecutors.diskIO().execute { localDataSource.updateOrder(list) }
    }

    override fun deleteOrder(id: Int) {
        appExecutors.diskIO().execute { localDataSource.deleteOrder(id) }
    }

    override fun searchOrder(cari: String): LiveData<PagedList<OrderEntity>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build()
        return LivePagedListBuilder(localDataSource.searchOrder(cari), config).build()
    }

    override fun searchTglIn(cari: String, dari: String, ke: String): LiveData<PagedList<OrderEntity>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build()
        return LivePagedListBuilder(localDataSource.searchTglIn(cari, dari, ke), config).build()
    }

    override fun searchTglOut(cari: String, dari: String, ke: String): LiveData<PagedList<OrderEntity>> {
        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(5)
                .setPageSize(5)
                .build()
        return LivePagedListBuilder(localDataSource.searchTglOut(cari, dari, ke), config).build()
    }
}