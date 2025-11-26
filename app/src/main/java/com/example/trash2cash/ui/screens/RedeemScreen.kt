package com.example.trash2cash.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
private val monthlyIncomeCategories = listOf(
    "Organik" to "Rp 50.000,00",
    "Plastik (Botol PET)" to "Rp 110.000,00",
    "Kaca" to "Rp 20.250,00",
    "Logam (Kaleng)" to "Rp 70.500,00"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(navController: NavController) {

    // Definisikan warna dan gradasi
    val colorStart = Color(0xFF40E0D0)
    val colorEnd = Color(0xFF3CB371)
    val primaryColor = Color.Black
    val accentColor = colorEnd

    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorStart, colorEnd),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pendapatan Bulan Ini", color = primaryColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tombol Kembali",
                            tint = primaryColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(gradientBrush)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Total Pendapatan (November 2025):",
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor
                )
                Text(
                    text = "Rp 250.750,00",
                    style = MaterialTheme.typography.headlineMedium,
                    color = accentColor
                )

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = primaryColor)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rincian Pendapatan Bulan Ini:",
                    style = MaterialTheme.typography.titleMedium,
                    color = primaryColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(monthlyIncomeCategories) { (category, amount) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = category, style = MaterialTheme.typography.bodyLarge, color = primaryColor)
                                Text(text = amount, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = accentColor)
                            }
                        }
                    }
                }
            }
        }
    }
}