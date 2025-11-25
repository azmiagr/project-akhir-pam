package com.example.project_akhir_pam.reminder

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar

class ReminderViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val _reminderList = MutableStateFlow<List<ReminderModel>>(emptyList())
    val reminderList: StateFlow<List<ReminderModel>> = _reminderList.asStateFlow()

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun addReminder(hour: Int, minute: Int) {
        val newItem = ReminderModel(hour = hour, minute = minute)
        _reminderList.value = _reminderList.value + newItem
        scheduleAlarm(newItem)
    }

    fun deleteReminder(item: ReminderModel) {
        cancelAlarm(item)
        _reminderList.value = _reminderList.value.filter { it.id != item.id }
    }

    fun toggleReminder(item: ReminderModel, isActive: Boolean) {
        _reminderList.value = _reminderList.value.map {
            if (it.id == item.id) it.copy(isActive = isActive) else it
        }

        if (isActive) {
            scheduleAlarm(item)
        } else {
            cancelAlarm(item)
        }
    }

    private fun scheduleAlarm(item: ReminderModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, item.hour)
            set(Calendar.MINUTE, item.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Toast.makeText(context, "Notifikasi diset jam ${item.hour}:${item.minute}", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(context, "Izin notifikasi belum diberikan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cancelAlarm(item: ReminderModel) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Notifikasi dibatalkan", Toast.LENGTH_SHORT).show()
    }
}