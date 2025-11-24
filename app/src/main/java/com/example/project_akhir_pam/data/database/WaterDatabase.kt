package com.example.project_akhir_pam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_akhir_pam.data.dao.WaterDao
import com.example.project_akhir_pam.data.entity.WaterRecord

@Database(entities = [WaterRecord::class], version = 1, exportSchema = false)
abstract class WaterDatabase : RoomDatabase() {
    abstract fun waterDao(): WaterDao

    companion object {
        @Volatile
        private var Instance: WaterDatabase? = null

        fun getDatabase(context: Context): WaterDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WaterDatabase::class.java, "water_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}