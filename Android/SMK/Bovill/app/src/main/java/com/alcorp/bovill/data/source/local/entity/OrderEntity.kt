package com.alcorp.bovill.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderentities")
data class OrderEntity (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_order")
    var id_order: Int,

    @ColumnInfo(name = "nama")
    var nama: String,

    @ColumnInfo(name = "tglorder")
    var tglorder: String,

    @ColumnInfo(name = "subtotal")
    var subtotal: String,

    @ColumnInfo(name = "diskon")
    var diskon: String,

    @ColumnInfo(name = "bayar")
    var bayar: String
)