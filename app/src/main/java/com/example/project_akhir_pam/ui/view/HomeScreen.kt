package com.example.project_akhir_pam.ui.view

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project_akhir_pam.ui.viewmodel.WaterViewModel

@Composable
fun HomeScreen(
    viewModel: WaterViewModel,
    modifier: Modifier = Modifier
) {
    val todayTotal by viewModel.todayTotal.collectAsState()
    val dailyTarget by viewModel.dailyTarget.collectAsState()
    val progressPercentage by viewModel.progressPercentage.collectAsState()

    val animatedProgress by animateFloatAsState(
        targetValue = progressPercentage/100f,
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
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 32.dp)
        ) {
            Text (
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

            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text (
                    text = "$todayTotal ml",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text (
                    text = "dari $dailyTarget ml",
                    fontSize = 18.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text (
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

        Column (
            modifier = Modifier.fillMaxWidth()
        ) {
            Text (
                text = "Tambah cepat",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row (
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

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxSize()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.secondary
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