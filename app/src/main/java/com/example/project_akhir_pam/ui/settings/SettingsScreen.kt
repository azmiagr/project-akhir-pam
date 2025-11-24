package com.example.project_akhir_pam.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.material.icons.filled.ArrowBack

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val target by viewModel.target.collectAsState()
    var text by remember { mutableStateOf(target.toString()) }

    LaunchedEffect(target) {
        text = target.toString()
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Target minum harian (ml)")
            TextField(
                value = text,
                onValueChange = { newText -> text = newText},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val newValue = text.filter { it.isDigit() }.toIntOrNull()
                    if (newValue != null) {
                        viewModel.updateTarget(newValue) }
                }
            ) {
                Text("Simpan")
            }
        }
    }
}