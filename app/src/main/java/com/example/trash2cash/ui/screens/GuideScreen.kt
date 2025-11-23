package com.example.trash2cash.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Panduan Pemilahan Sampah", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Gunakan LazyColumn agar bisa di-scroll jika daftarnya panjang
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(sortingGuideList) { (title, description) ->
                // Gunakan Card agar setiap item terlihat terpisah
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = description, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}