package com.alcorp.aryunas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kamar_entity")
data class KamarEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_kamar")
    var id_kamar: Int = 0,

    @ColumnInfo(name = "nama_kamar")
    var nama_kamar: String? = null,

    @ColumnInfo(name = "harga_kamar")
    var harga_kamar: Int? = null
)