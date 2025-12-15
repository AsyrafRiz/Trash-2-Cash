package com.example.trash2cash.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideScreen(navController: NavController) {

    val colorStart = Color(0xFF40E0D0)
    val colorEnd = Color(0xFF3CB371)
    val darkTextColor = Color(0xFF1E5128)

    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorStart, colorEnd),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panduan Pemilahan Sampah") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(gradientBrush)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sortingGuideList) { (title, description) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = darkTextColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
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