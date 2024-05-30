package com.alcorp.aryunas.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.alcorp.aryunas.data.local.entity.KamarEntity
import com.alcorp.aryunas.data.local.entity.Order
import com.alcorp.aryunas.data.local.entity.OrderDetail

@Dao
interface AppDao {
    // Kamar
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertKamar(kamarEntity: KamarEntity)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateKamar(kamarEntity: KamarEntity)

    @Query("DELETE FROM kamar_entity WHERE id_kamar = :id_kamar")
    fun deleteKamar(id_kamar: Int)

    @Query("SELECT * FROM kamar_entity ORDER BY id_kamar DESC")
    fun getKamar() : DataSource.Factory<Int, KamarEntity>

    @Transaction
    @Query("SELECT * FROM kamar_entity WHERE id_kamar = :id_kamar")
    fun getKamarById(id_kamar: Int): LiveData<KamarEntity>

    @Query("SELECT COUNT(id_kamar) FROM order_detail_entity WHERE id_kamar = :id_kamar")
    fun countKamar(id_kamar: Int) : Int

    // Order
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrder(orderEntity: Order)

    @Query("DELETE FROM order_entity WHERE id_order = :id_order")
    fun deleteOrder(id_order: Int)

    @Query("SELECT * FROM order_entity ORDER BY id_order DESC")
    fun getOrder() : DataSource.Factory<Int, Order>

    @Query("SELECT COUNT(*) FROM order_entity")
    fun countOrder() : Int

    @Transaction
    @Query("SELECT * FROM order_entity WHERE id_order = :id_order")
    fun getOrderById(id_order: Int): LiveData<Order>

    @Query("SELECT * FROM order_entity ORDER BY id_order DESC LIMIT 1")
    fun getLatestOrder(): List<Order>

    @Query("SELECT * FROM order_entity WHERE nama LIKE '%' || :cari || '%' ORDER BY id_order DESC")
    fun searchOrder(cari: String): DataSource.Factory<Int, Order>

    @Query("SELECT * FROM order_entity WHERE nama LIKE '%' || :cari || '%' AND tgl_order BETWEEN :dari AND :ke ORDER BY id_order DESC")
    fun searchTglOrder(cari: String, dari: String, ke: String): DataSource.Factory<Int, Order>

    // Order Detail
    @Query("SELECT * FROM kamar_entity ORDER BY id_kamar DESC")
    fun getKamarBooking() : List<KamarEntity>

    @Query("SELECT * FROM order_detail_entity WHERE id_order = :id_order ORDER BY id_order DESC")
    fun getOrderDetailByIdOrderList(id_order: Int) : List<OrderDetail>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrderDetail(orderDetailEntity: OrderDetail)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updateOrderDetail(orderDetailEntity: OrderDetail)

    @Query("DELETE FROM order_detail_entity WHERE id_order_detail = :id_order_detail")
    fun deleteOrderDetail(id_order_detail: Int)

    @Query("DELETE FROM order_detail_entity WHERE id_order = :id_order")
    fun deleteOrderDetailByIdOrder(id_order: Int)

    @Query("SELECT * FROM order_detail_entity ORDER BY id_order_detail DESC")
    fun getOrderDetail() : DataSource.Factory<Int, OrderDetail>

    @Query("SELECT * FROM order_detail_entity WHERE id_order = :id_order ORDER BY id_order_detail DESC")
    fun getOrderDetailByIdOrder(id_order: Int) : DataSource.Factory<Int, OrderDetail>

    @Transaction
    @Query("SELECT * FROM order_detail_entity WHERE id_order = :id_order")
    fun getOrderDetailById(id_order: Int): LiveData<OrderDetail>
}