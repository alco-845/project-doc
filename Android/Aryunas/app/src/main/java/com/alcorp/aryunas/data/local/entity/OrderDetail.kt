package com.alcorp.aryunas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_detail_entity")
data class OrderDetail (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_order_detail")
    var id_order_detail: Int = 0,

    @ColumnInfo(name = "id_order")
    var id_order: Int = 0,

    @ColumnInfo(name = "id_kamar")
    var id_kamar: Int = 0,

    @ColumnInfo(name = "nama_kamar")
    var nama_kamar: String,

    @ColumnInfo(name = "tglin")
    var tglin: String,

    @ColumnInfo(name = "jamin")
    var jamin: String,

    @ColumnInfo(name = "tglout")
    var tglout: String,

    @ColumnInfo(name = "jamout")
    var jamout: String,

    @ColumnInfo(name = "total_hari")
    var total_hari: Int,

    @ColumnInfo(name = "total_harga")
    var total_harga: Int,

    @ColumnInfo(name = "harga_kamar")
    var harga_kamar: Int
)