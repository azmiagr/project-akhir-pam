package com.example.project_akhir_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_akhir_pam.data.entity.WaterRecord
import com.example.project_akhir_pam.data.repository.UserPreferenceRepository
import com.example.project_akhir_pam.repository.WaterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class WaterViewModel(
    private val repository: WaterRepository,
    private val prefRepo: UserPreferenceRepository) : ViewModel() {
    private val _dailyTarget = MutableStateFlow(2000)
    val dailyTarget: StateFlow<Int> = _dailyTarget.asStateFlow()

    private val _todayTotal = MutableStateFlow(0)
    val todayTotal: StateFlow<Int> = _todayTotal.asStateFlow()

    private val _progressPercentage = MutableStateFlow(0f)
    val progressPercentage: StateFlow<Float> = _progressPercentage.asStateFlow()

    private val _historyRecords = MutableStateFlow<List<WaterRecord>>(emptyList())
    val historyRecords: StateFlow<List<WaterRecord>> = _historyRecords.asStateFlow()

    init {
        loadTodayTotal()
        loadHistory()

        viewModelScope.launch {
            prefRepo.getTarget()
                .catch {
                    // FIX: Jika error (database kosong), emit default 2000
                    emit(2000)
                }
                .collect { newTarget ->
                    // FIX: Validasi target tidak pernah 0 atau negatif
                    _dailyTarget.value = if (newTarget <= 0) 2000 else newTarget
                    calculateProgress()
                }
        }
    }

    private fun loadTodayTotal() {
        viewModelScope.launch {
            val todayDate = getCurrentDate()
            repository.getTodayTotal(todayDate).collect { total ->
                _todayTotal.value = total ?: 0
                calculateProgress()
            }
        }
    }

    private fun loadHistory() {
        viewModelScope.launch {
            repository.getAllHistory().collect { records ->
                _historyRecords.value = records
            }
        }
    }

    private fun calculateProgress() {
        val target = _dailyTarget.value.toFloat()
        val total = _todayTotal.value.toFloat()

        if (target <= 0) {
            _progressPercentage.value = 0f
            return
        }

        val progress = (total / target) * 100f

        _progressPercentage.value = when {
            progress.isNaN() || progress.isInfinite() -> 0f
            else -> progress.coerceIn(0f, 100f)
        }
    }

    fun addWater(amount: Int) {
        viewModelScope.launch {
            val waterRecord = WaterRecord(
                jumlahAir = amount,
                waktu = getCurrentTime(),
                tanggal = getCurrentDate()
            )
            repository.insertWater(waterRecord)
        }
    }

    fun deleteWater(waterRecord: WaterRecord) {
        viewModelScope.launch {
            repository.deleteWater(waterRecord)
        }
    }

    fun updateDailyTarget(newTarget: Int) {
        // Validasi: pastikan target minimal 1
        val validTarget = if (newTarget <= 0) 2000 else newTarget
        _dailyTarget.value = validTarget
        calculateProgress()
    }

    private fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}