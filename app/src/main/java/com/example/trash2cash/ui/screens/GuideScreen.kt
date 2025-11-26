package com.example.trash2cash.ui.screens

import androidx.compose.foundation.background // Import untuk background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush // Import untuk Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
private val sortingGuideList = listOf(
    "1. Sampah Organik" to "Sisa makanan, daun, ranting.",
    "2. Sampah Anorganik" to "Plastik, logam, kaca.",
    "3. Sampah B3" to "Baterai, lampu neon, bahan kimia.",
    "4. Sampah Elektronik" to "Ponsel, TV, perangkat rusak.",
    "5. Sampah Residual" to "Sampah yang tidak dapat didaur ulang.",
    "6. Sampah Konstruksi" to "Beton, kayu, sisa bangunan.",
    "7. Sampah Rumah Tangga Lainnya" to "Pakaian bekas, kemasan."
)

@Composable
fun GuideScreen(navController: NavController) {

    // Definisikan warna untuk gradasi
    val colorStart = Color(0xFF40E0D0) // Biru Muda/Cyan
    val colorEnd = Color(0xFF3CB371)   // Hijau Daun
    val darkTextColor = Color(0xFF1E5128) // Hijau Tua untuk kontras pada Card

    // Buat Brush untuk gradasi linear
    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorStart, colorEnd),
    )

    // PERUBAHAN UTAMA: Box untuk menerapkan gradasi
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush) // Menerapkan gradasi
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Judul diubah menjadi Putih agar terlihat jelas
            Text(
                text = "Panduan Pemilahan Sampah",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(sortingGuideList) { (title, description) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        // Card diatur agar memiliki latar belakang Putih solid
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Judul Card menggunakan warna gelap
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = darkTextColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            // Deskripsi Card menggunakan warna gelap
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}