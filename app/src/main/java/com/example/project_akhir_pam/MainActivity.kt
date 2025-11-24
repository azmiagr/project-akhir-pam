package com.example.project_akhir_pam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.project_akhir_pam.data.database.WaterDatabase
import com.example.project_akhir_pam.repository.WaterRepository
import com.example.project_akhir_pam.ui.view.MainScreen
import com.example.project_akhir_pam.ui.theme.Project_Akhir_PAMTheme
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WaterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = WaterDatabase.getDatabase(applicationContext)
        val repository = WaterRepository(database.waterDao())
        val viewModelFactory = WaterViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory)[WaterViewModel::class.java]

        setContent {
            Project_Akhir_PAMTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}