package com.example.project_akhir_pam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.project_akhir_pam.data.database.UserPreferenceDatabase
import com.example.project_akhir_pam.data.database.WaterDatabase
import com.example.project_akhir_pam.data.repository.UserPreferenceRepository
import com.example.project_akhir_pam.repository.WaterRepository
import com.example.project_akhir_pam.sensor.ShakeSensor
import com.example.project_akhir_pam.sensor.ShakeSensorImpl
import com.example.project_akhir_pam.ui.theme.Project_Akhir_PAMTheme
import com.example.project_akhir_pam.ui.view.MainScreen
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WaterViewModel
    private lateinit var shakeSensor: ShakeSensor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = WaterDatabase.getDatabase(applicationContext)
        val repository = WaterRepository(database.waterDao())
        val prefDatabase = UserPreferenceDatabase.getInstance(applicationContext)
        val prefRepo = UserPreferenceRepository(prefDatabase.userPreferenceDao())

        val viewModelFactory = WaterViewModelFactory(
            repository = repository,
            prefRepo = prefRepo
        )


        viewModel = ViewModelProvider(this, viewModelFactory)[WaterViewModel::class.java]

        shakeSensor = ShakeSensorImpl(this)
        shakeSensor.start()

        lifecycleScope.launch {
            shakeSensor.onShake.collect {
                viewModel.addWater(200)
            }
        }

        setContent {
            Project_Akhir_PAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel = viewModel)
                }
            }
        }
    }
}
