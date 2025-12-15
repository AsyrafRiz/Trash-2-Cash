@file:JvmName("HomeScreenKt")

package com.example.trash2cash.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trash2cash.R
import com.example.trash2cash.logoutUser
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape

data class WasteHistoryItem(
    val name: String,
    val quantity: String,
    val price: String,
    val date: String
)
private val latestHistory = listOf(
    WasteHistoryItem("Botol Aqua 750 ml", "10 botol", "+ Rp 1.200", "01 Nov 2025"),
    WasteHistoryItem("Kaleng Coca-Cola", "5 kaleng", "+ Rp 1.000", "01 Nov 2025"),
    WasteHistoryItem("Kardus Box Indomie", "2 box", "+ Rp 2.000", "31 Okt 2025"),
    WasteHistoryItem("Botol Kaca Kecap", "3 botol", "+ Rp 1.050", "30 Okt 2025"),
    WasteHistoryItem("Botol Aqua 1500 ml", "8 botol", "+ Rp 1.280", "30 Okt 2025"),
    WasteHistoryItem("Gelas Plastik", "50 gelas", "+ Rp 1.250", "29 Okt 2025")
)

@Composable
fun HomeScreen(navController: NavController) {
    val colorStart = Color(0xFF40E0D0)
    val colorEnd = Color(0xFF3CB371)

    val textColorOnGradient = Color.White
    val cardBackgroundColor = Color.White
    val cardTextColor = Color.Black

    val gradientBrush = Brush.linearGradient(
        colors = listOf(colorStart, colorEnd),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        // Logo Latar Belakang
        Image(
            painter = painterResource(id = R.drawable.icon_bank_sampah),
            contentDescription = "Background Logo",
            modifier = Modifier.align(Alignment.Center).size(350.dp).alpha(0.1f),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp)) // Menambahkan jarak di atas

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selamat Datang!",
                    style = MaterialTheme.typography.titleLarge,
                    color = textColorOnGradient
                )
                TextButton(onClick = {
                    logoutUser(navController)
                }) {
                    Text("Log Out", color = textColorOnGradient)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu
            Text(text = "Menu", style = MaterialTheme.typography.titleMedium, color = textColorOnGradient)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Mengatur jarak antar tombol
            ) {
                MenuButton(navController = navController, route = "redeem", iconRes = R.drawable.icon_reedem_point, text = "Redeem")
                MenuButton(navController = navController, route = "location", iconRes = R.drawable.icon_lokasi, text = "Lokasi")
                MenuButton(navController = navController, route = "guide", iconRes = R.drawable.icon_panduan, text = "Panduan")
                MenuButton(navController = navController, route = "scan", iconRes = R.drawable.icon_scan, text = "Scan")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // History
            Text(text = "History Terakhir", style = MaterialTheme.typography.titleMedium, color = textColorOnGradient)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(latestHistory) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = cardTextColor
                                )
                                Text(
                                    text = "${item.quantity} (${item.date})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = cardTextColor
                                )
                            }
                            Text(
                                text = item.price,
                                style = MaterialTheme.typography.bodyLarge,
                                color = cardTextColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.MenuButton(
    navController: NavController,
    route: String,
    iconRes: Int,
    text: String
) {
    Button(
        onClick = { navController.navigate(route) },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.weight(1f).height(110.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = "$text Icon",
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text)
        }
    }
}