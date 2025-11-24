package com.example.project_akhir_pam.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.project_akhir_pam.data.entity.WaterRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Insert
    suspend fun insertWater(waterRecord: WaterRecord)

    @Query("SELECT * FROM water_records ORDER BY id DESC")
    fun getAllHistory(): Flow<List<WaterRecord>>

    @Query("SELECT SUM(jumlahAir) FROM water_records WHERE tanggal = :todayDate")
    fun getTodayTotal(todayDate: String): Flow<Int?>

    @Delete
    suspend fun deleteWater(waterRecord: WaterRecord)
}