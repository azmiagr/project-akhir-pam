package com.example.project_akhir_pam.data.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferenceRepository {
    fun getTarget(): Flow<Int>
    suspend fun setTarget(value: Int)
}