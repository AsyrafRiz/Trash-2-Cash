// File: com/example/trash2cash/ui/screens/LocationScreen.kt
package com.example.trash2cash.ui.screens

import androidx.compose.foundation.Image // <-- Import ini penting
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource // <-- Import ini penting
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trash2cash.R // <-- Import R untuk mengakses drawable

@Composable
fun LocationScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) // Kita beri padding agar rapi
    ) {
        Text(
            text = "Lokasi Bank Sampah",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp)) // Beri jarak

        // Gunakan Composable 'Image' standar
        Image(
            // Ganti R.drawable.img_maps dengan nama file Anda
            // (misal: R.drawable.peta_bank_sampah)
            painter = painterResource(id = R.drawable.map),

            contentDescription = "Peta Lokasi Bank Sampah",
            modifier = Modifier.fillMaxWidth(),

            // ContentScale.Crop akan membuat gambar memenuhi lebar
            // tanpa merusak rasio aspek
            contentScale = ContentScale.Crop
        )
    }
}