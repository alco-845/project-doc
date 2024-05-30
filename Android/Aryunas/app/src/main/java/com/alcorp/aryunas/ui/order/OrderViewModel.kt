package com.alcorp.aryunas.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.alcorp.aryunas.data.AppRepository
import com.alcorp.aryunas.data.local.entity.Order
import com.alcorp.aryunas.data.local.entity.OrderDetail

class OrderViewModel(private val appRepository: AppRepository) : ViewModel() {
    var order: LiveData<PagedList<Order>>? = null
        get() {
            if (field == null) {
                field = getOrder()
            }
            return field
        }
        private set

    @JvmName("getOrder1")
    fun getOrder(): LiveData<PagedList<Order>> = appRepository.getOrder()

    var id_order = MutableLiveData<Int>()

    fun setSelectedOrder(id_order: Int) {
        this.id_order.value = id_order
    }

    var orderEntityById: LiveData<Order> = Transformations.switchMap(id_order) { id_order ->
        appRepository.getOrderById(id_order)
    }

    fun countOrder() = appRepository.countOrder()
    fun insertOrder(order: Order) = appRepository.insertOrder(order)
    fun deleteOrder(id_order: Int) = appRepository.deleteOrder(id_order)

    fun searchOrder(cari: String) : LiveData<PagedList<Order>> = appRepository.searchOrder(cari)

    fun searchTglOrder(cari: String, dari: String, ke: String) : LiveData<PagedList<Order>> = appRepository.searchTglOrder(cari, dari, ke)

    var orderDetail: LiveData<PagedList<OrderDetail>>? = null
        get() {
            if (field == null) {
                field = getOrderDetail()
            }
            return field
        }
        private set

    @JvmName("getOrderDetail1")
    fun getOrderDetail(): LiveData<PagedList<OrderDetail>> = appRepository.getOrderDetail()

    var getOrderDetailByIdOrder: LiveData<PagedList<OrderDetail>> = Transformations.switchMap(id_order) { id_order ->
        appRepository.getOrderDetailByIdOrder(id_order)
    }

    fun getOrderDetailByIdOrderList(id_order: Int): List<OrderDetail> = appRepository.getOrderDetailByIdOrderList(id_order)
    fun insertOrderDetail(orderDetail: OrderDetail) = appRepository.insertOrderDetail(orderDetail)
    fun deleteOrderDetail(id_order_detail: Int) = appRepository.deleteOrderDetail(id_order_detail)
    fun deleteOrderDetailByIdOrder(id_order: Int) = appRepository.deleteOrderDetailByIdOrder(id_order)
}