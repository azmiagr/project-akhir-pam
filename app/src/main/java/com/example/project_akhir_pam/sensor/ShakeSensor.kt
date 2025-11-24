package com.example.project_akhir_pam.sensor

import kotlinx.coroutines.flow.Flow

interface ShakeSensor {
    val onShake: Flow<Unit>
    fun start()
    fun stop()
}