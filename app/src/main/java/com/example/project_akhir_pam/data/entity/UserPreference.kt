package com.example.project_akhir_pam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreference(
    @PrimaryKey val id: Int = 1,
    val dailyTarget: Int
)
