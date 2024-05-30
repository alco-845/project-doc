package com.alcorp.aryunas.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alcorp.aryunas.data.local.AppDao
import com.alcorp.aryunas.data.local.entity.KamarEntity
import com.alcorp.aryunas.data.local.entity.Order
import com.alcorp.aryunas.data.local.entity.OrderDetail

class AppRepository(private val appDao: AppDao) {
    // Kamar
    fun getKamar(): LiveData<PagedList<KamarEntity>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(appDao.getKamar(), config).build()
    }

    fun insertKamar(kamarEntity: KamarEntity) = appDao.insertKamar(kamarEntity)
    fun updateKamar(kamarEntity: KamarEntity) = appDao.updateKamar(kamarEntity)
    fun deleteKamar(id_kamar: Int) = appDao.deleteKamar(id_kamar)
    fun getKamarById(id: Int): LiveData<KamarEntity> = appDao.getKamarById(id)
    fun countKamar(id_kamar: Int) = appDao.countKamar(id_kamar)

    // Order
    fun getOrder(): LiveData<PagedList<Order>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(appDao.getOrder(), config).build()
    }

    fun countOrder() = appDao.countOrder()
    fun insertOrder(orderEntity: Order) = appDao.insertOrder(orderEntity)
    fun deleteOrder(id_order: Int) = appDao.deleteOrder(id_order)
    fun getOrderById(id: Int): LiveData<Order> = appDao.getOrderById(id)

    fun searchOrder(cari: String): LiveData<PagedList<Order>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(5)
            .setPageSize(5)
            .build()
        return LivePagedListBuilder(appDao.searchOrder(cari), config).build()
    }

    fun searchTglOrder(cari: String, dari: String, ke: String): LiveData<PagedList<Order>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(appDao.searchTglOrder(cari, dari, ke), config).build()
    }

    // Order Detail
    fun getOrderDetail(): LiveData<PagedList<OrderDetail>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(appDao.getOrderDetail(), config).build()
    }

    fun getOrderDetailByIdOrder(id_order: Int): LiveData<PagedList<OrderDetail>> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(appDao.getOrderDetailByIdOrder(id_order), config).build()
    }

    fun getKamarBooking() = appDao.getKamarBooking()

    fun insertOrderDetail(orderDetail: OrderDetail) = appDao.insertOrderDetail(orderDetail)
    fun updateOrderDetail(orderDetail: OrderDetail) = appDao.updateOrderDetail(orderDetail)
    fun deleteOrderDetail(id_order_detail: Int) = appDao.deleteOrderDetail(id_order_detail)
    fun deleteOrderDetailByIdOrder(id_order: Int) = appDao.deleteOrderDetailByIdOrder(id_order)
    fun getOrderDetailByIdOrderList(id_order: Int) = appDao.getOrderDetailByIdOrderList(id_order)
    fun getOrderDetailById(id: Int): LiveData<OrderDetail> = appDao.getOrderDetailById(id)
}