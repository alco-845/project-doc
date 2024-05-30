package com.alcorp.bovill.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.alcorp.bovill.data.source.local.entity.OrderEntity

@Dao
interface AppDao {
    @Query("SELECT * FROM orderentities ORDER BY id_order DESC")
    fun getListOrder(): DataSource.Factory<Int, OrderEntity>

    @Transaction
    @Query("SELECT * FROM orderentities WHERE id_order = :id_order")
    fun getOrder(id_order: Int): LiveData<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrder(booking: List<OrderEntity>)

    @Update
    fun updateOrder(booking: List<OrderEntity>)

    @Query("DELETE FROM orderentities WHERE id_order = :id_order")
    fun deleteOrder(id_order: Int)

    @Query("SELECT * FROM orderentities WHERE nama LIKE '%' || :cari || '%' ORDER BY id_order DESC")
    fun searchOrder(cari: String): DataSource.Factory<Int, OrderEntity>

    @Query("SELECT * FROM orderentities WHERE nama LIKE '%' || :cari || '%' AND tglorder BETWEEN :dari AND :ke ORDER BY id_order DESC")
    fun searchTglIn(cari: String, dari: String, ke: String): DataSource.Factory<Int, OrderEntity>

    @Query("SELECT * FROM orderentities WHERE nama LIKE '%' || :cari || '%' AND tglorder BETWEEN :dari AND :ke ORDER BY id_order DESC")
    fun searchTglOut(cari: String, dari: String, ke: String): DataSource.Factory<Int, OrderEntity>
}