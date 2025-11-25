package com.example.project_akhir_pam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_akhir_pam.data.dao.UserPreferenceDao
import com.example.project_akhir_pam.data.entity.UserPreference

@Database(
    entities = [UserPreference::class],
    version = 1,
    exportSchema = false
)
abstract class UserPreferenceDatabase : RoomDatabase() {

    abstract fun userPreferenceDao(): UserPreferenceDao

    companion object {
        @Volatile
        private var INSTANCE: UserPreferenceDatabase? = null

        fun getInstance(context: Context): UserPreferenceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserPreferenceDatabase::class.java,
                    "user_preference_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
