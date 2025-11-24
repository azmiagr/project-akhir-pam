package com.example.project_akhir_pam.repository

import com.example.project_akhir_pam.data.dao.WaterDao
import com.example.project_akhir_pam.data.entity.WaterRecord
import kotlinx.coroutines.flow.Flow

class WaterRepository(private val waterDao: WaterDao) {
    fun getAllHistory(): Flow<List<WaterRecord>> = waterDao.getAllHistory()

    fun getTodayTotal(todayDate: String): Flow<Int?> = waterDao.getTodayTotal(todayDate)

    suspend fun insertWater(waterRecord: WaterRecord) {
        waterDao.insertWater(waterRecord)
    }

    suspend fun deleteWater(waterRecord: WaterRecord) {
        waterDao.deleteWater(waterRecord)
    }
}