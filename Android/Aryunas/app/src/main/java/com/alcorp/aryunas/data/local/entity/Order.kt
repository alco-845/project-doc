package com.alcorp.aryunas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_entity")
data class Order (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id_order")
    var id_order: Int = 0,

    @ColumnInfo(name = "nama")
    var nama: String? = null,

    @ColumnInfo(name = "tgl_order")
    var tgl_order: String? = null,

    @ColumnInfo(name = "subtotal")
    var subtotal: Int? = null,

    @ColumnInfo(name = "diskon")
    var diskon: Int? = null,

    @ColumnInfo(name = "total")
    var total: Int? = null,

    @ColumnInfo(name = "bayar")
    var bayar: Int? = null,

    @ColumnInfo(name = "kembali")
    var kembali: Int? = null
)
