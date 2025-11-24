// File: com/example/trash2cash/ui/screens/RedeemScreen.kt
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
// Kita tidak lagi butuh import Firebase atau RedeemItem di sini

// Data Statis (Pura-pura) untuk Pendapatan Bulan November
private val monthlyIncomeCategories = listOf(
    "Organik" to "Rp 50.000,00",
    "Plastik (Botol PET)" to "Rp 110.000,00",
    "Kaca" to "Rp 20.250,00",
    "Logam (Kaleng)" to "Rp 70.500,00"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pendapatan Bulan Ini") }, // JUDUL BARU
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Tombol Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {

            // --- BAGIAN TOTAL PENDAPATAN BULAN INI ---
            Text(
                text = "Total Pendapatan (November 2025):", // TEKS BARU
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Rp 250.750,00", // JUMLAH BARU
                style = MaterialTheme.typography.headlineMedium,
                color = Color(0xFF006400) // Warna hijau tua
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider() // Garis pemisah
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Rincian Pendapatan Bulan Ini:", // TEKS BARU
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- BAGIAN DAFTAR (SCROLLABLE) ---
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Gunakan data pendapatan bulanan
                items(monthlyIncomeCategories) { (category, amount) ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = category, style = MaterialTheme.typography.bodyLarge)
                            Text(text = amount, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}