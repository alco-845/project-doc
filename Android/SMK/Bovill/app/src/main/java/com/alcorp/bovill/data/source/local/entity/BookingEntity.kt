package com.alcorp.bovill.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookingentities")
data class BookingEntity (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id_booking")
    var id_booking: Int,

    @ColumnInfo(name = "id_order")
    var id_order: Int,

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
    var total_hari: String,

    @ColumnInfo(name = "harga")
    var harga: String
)