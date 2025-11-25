package com.example.project_akhir_pam.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferenceRepositoryImpl(private val context: Context) : UserPreferenceRepository {

    private val TARGET_KEY = intPreferencesKey("daily_target")

    override fun getTarget(): Flow<Int> =
        context.dataStore.data.map { prefs -> prefs[TARGET_KEY] ?: 2000 }

    override suspend fun setTarget(value: Int) {
        context.dataStore.edit { prefs ->
            prefs[TARGET_KEY] = value
        }
    }
}