package com.example.project_akhir_pam

import android.content.Context
import com.example.project_akhir_pam.data.database.WaterDatabase
import com.example.project_akhir_pam.repository.WaterRepository

interface AppContainer {
    val waterRepository: WaterRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val waterRepository: WaterRepository by lazy {
        val database = WaterDatabase.getDatabase(context)
        WaterRepository(database.waterDao())
    }
}