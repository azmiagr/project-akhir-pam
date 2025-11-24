package com.example.project_akhir_pam.ui.view

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel
import kotlin.math.sin

@Composable
fun HomeScreen(
    viewModel: WaterViewModel,
    modifier: Modifier = Modifier
) {
    val todayTotal by viewModel.todayTotal.collectAsState()
    val dailyTarget by viewModel.dailyTarget.collectAsState()
    val progressPercentage by viewModel.progressPercentage.collectAsState()

    val context = LocalContext.current

    var showCustomDialog by remember { mutableStateOf(false) }

    if (showCustomDialog) {
        CustomWaterDialog(
            onDismiss = { showCustomDialog = false },
            onConfirm = { amount ->
                viewModel.addWater(amount)
                Toast.makeText(context, "Berhasil menambah $amount ml", Toast.LENGTH_SHORT).show()
                showCustomDialog = false
            }
        )
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercentage / 100f,
        animationSpec = tween(1000),
        label = "progress_animation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text(
                text = "Konsumsi Air Hari Ini",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tetap terhidrasi untuk kesehatan",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(280.dp)
        ) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.fillMaxSize(),
                color = if (100f <= progressPercentage) {
                    Color(0xFF4CAF50)
                } else MaterialTheme.colorScheme.primary,
                strokeWidth = 20.dp,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$todayTotal ml",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "dari $dailyTarget ml",
                    fontSize = 18.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${progressPercentage.toInt()}%",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (100f <= progressPercentage) {
                        Color(0xFF4CAF50)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Tambah cepat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickAddButton(
                    amount = 100,
                    onClick = { viewModel.addWater(100) },
                    modifier = Modifier.weight(1f)
                )

                QuickAddButton(
                    amount = 250,
                    onClick = { viewModel.addWater(250) },
                    modifier = Modifier.weight(1f)
                )

                QuickAddButton(
                    amount = 500,
                    onClick = { viewModel.addWater(500) },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showCustomDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Text(
                    text = "Jumlah Custom",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun QuickAddButton (
    amount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text (
                text = "+$amount",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text (
                text = "ml",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun CustomWaterDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var amountText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Masukkan Jumlah Air") },
        text = {
            OutlinedTextField(
                value = amountText,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        amountText = newValue
                    }
                },
                label = { Text("Jumlah (ml)") },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountText.toIntOrNull()
                    if (amount != null && amount > 0) {
                        onConfirm(amount)
                    }
                }
            ) {
                Text("Tambah")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}