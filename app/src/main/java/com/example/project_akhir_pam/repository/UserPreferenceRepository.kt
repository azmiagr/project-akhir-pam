package com.example.project_akhir_pam.data.repository

import com.example.project_akhir_pam.data.dao.UserPreferenceDao
import com.example.project_akhir_pam.data.entity.UserPreference
import kotlinx.coroutines.flow.Flow

class UserPreferenceRepository(
    private val dao: UserPreferenceDao
) {

    fun getTarget(): Flow<Int> = dao.getTarget()

    suspend fun setTarget(value: Int) {
        dao.insertPreference(
            UserPreference(
                id = 1,
                dailyTarget = value
            )
        )
    }
}
