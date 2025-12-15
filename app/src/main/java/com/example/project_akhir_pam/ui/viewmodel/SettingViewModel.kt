package com.example.project_akhir_pam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_akhir_pam.data.repository.UserPreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repo: UserPreferenceRepository
) : ViewModel() {

    val target = repo.getTarget().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        2000
    )

    fun updateTarget(v: Int) {
        viewModelScope.launch {
            repo.setTarget(v)
        }
    }
}
