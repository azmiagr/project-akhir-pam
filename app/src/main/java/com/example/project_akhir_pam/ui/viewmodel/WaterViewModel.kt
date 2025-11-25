package com.example.project_akhir_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_akhir_pam.data.entity.WaterRecord
import com.example.project_akhir_pam.data.repository.UserPreferenceRepository
import com.example.project_akhir_pam.repository.WaterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
            prefRepo.getTarget().collect { newTarget ->
                _dailyTarget.value = newTarget
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
        val progress = (_todayTotal.value.toFloat() / _dailyTarget.value.toFloat()) * 100
        _progressPercentage.value = progress.coerceIn(0f, 100f)
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
        _dailyTarget.value = newTarget
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