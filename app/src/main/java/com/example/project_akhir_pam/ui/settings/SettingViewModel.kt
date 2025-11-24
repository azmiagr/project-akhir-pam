package com.example.project_akhir_pam.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_akhir_pam.data.preferences.UserPreferenceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: UserPreferenceRepository
) : ViewModel() {

    private val _target = MutableStateFlow(2000)
    val target: StateFlow<Int> = _target

    init {
        viewModelScope.launch {
            repo.getTarget().collect { _target.value = it }
        }
    }

    fun updateTarget(newValue: Int) {
        viewModelScope.launch {
            repo.setTarget(newValue)
            repo.getTarget().collect { _target.value = it } // refresh
        }
    }
}