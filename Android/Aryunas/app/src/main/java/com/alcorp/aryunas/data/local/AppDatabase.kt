package com.alcorp.aryunas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alcorp.aryunas.data.local.entity.KamarEntity
import com.alcorp.aryunas.data.local.entity.Order
import com.alcorp.aryunas.data.local.entity.OrderDetail

@Database(entities = [KamarEntity::class, Order::class, OrderDetail::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "aryunas"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}