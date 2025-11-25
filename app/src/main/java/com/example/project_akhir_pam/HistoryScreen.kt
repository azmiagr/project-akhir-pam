package com.example.project_akhir_pam

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_akhir_pam.data.entity.WaterRecord
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel

@Composable
fun HistoryScreen(viewModel: WaterViewModel) {
    val history by viewModel.historyRecords.collectAsState()

    val sortedHistory = history.sortedByDescending { it.id }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(sortedHistory) { record ->
            HistoryItem(record)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HistoryItem(record: WaterRecord) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${record.jumlahAir} ml",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Waktu: ${record.waktu}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tanggal: ${record.tanggal}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
