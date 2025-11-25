package com.example.project_akhir_pam.reminder

data class ReminderModel(
    val id: Long = System.currentTimeMillis(), // id otomatis
    val hour: Int,
    val minute: Int,
    var isActive: Boolean = true
)