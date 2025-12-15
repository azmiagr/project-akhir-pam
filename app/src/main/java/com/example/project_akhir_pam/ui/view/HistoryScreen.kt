package com.example.project_akhir_pam.ui.view

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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.project_akhir_pam.data.entity.WaterRecord
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: WaterViewModel) {
    val history by viewModel.historyRecords.collectAsState()
    val sortedHistory = history.sortedByDescending { it.id }

    val groupedHistory = sortedHistory.groupBy { it.tanggal }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Riwayat Minum") },
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            groupedHistory.forEach { (tanggal, records) ->

                item {
                    Text(
                        text = formatTanggal(tanggal),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                val isToday = formatTanggal(tanggal) == "Hari Ini"
                if (!isToday) {
                    val total = records.sumOf { it.jumlahAir }
                    val liter = total / 1000.0

                    item {
                        Text(
                            text = "Total minum: ${"%.1f".format(liter)} liter",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                items(records) { record ->
                    HistoryItem(record)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
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

fun formatTanggal(tanggal: String): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val date = LocalDate.parse(tanggal, formatter)
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (date) {
        today -> "Hari Ini"
        yesterday -> "Kemarin"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}