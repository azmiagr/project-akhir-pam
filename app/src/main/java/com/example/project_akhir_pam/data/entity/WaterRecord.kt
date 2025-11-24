package com.example.project_akhir_pam.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_records")
data class WaterRecord(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val jumlahAir: Int,
        val waktu: String,
        val tanggal: String,
);