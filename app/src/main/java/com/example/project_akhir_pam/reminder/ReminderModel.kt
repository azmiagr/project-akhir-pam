package com.example.project_akhir_pam.reminder

data class ReminderModel(
    val id: Long = System.currentTimeMillis(),
    val hour: Int,
    val minute: Int,
    var isActive: Boolean = true
)