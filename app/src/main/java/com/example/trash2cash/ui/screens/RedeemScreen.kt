// File: com/example/trash2cash/ui/screens/HomeScreen.kt
package com.example.trash2cash.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trash2cash.R
import com.example.trash2cash.logoutUser
import androidx.compose.material3.ButtonDefaults

// --- DATA STATIS BARU (HISTORY TRANSAKSI) ---
// Kita buat data class baru untuk item history
data class WasteHistoryItem(
    val name: String,
    val quantity: String,
    val price: String, // Total harga untuk kuantitas itu
    val date: String
)

// Daftar history statis (pura-pura) dengan harga per item yang masuk akal
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
    val darkGreenColor = Color(0xFF006400)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Gambar Background Logo di Tengah (Watermark)
        Image(
            painter = painterResource(id = R.drawable.icon_bank_sampah),
            contentDescription = "Background Logo",
            modifier = Modifier.align(Alignment.Center).size(250.dp).alpha(0.1f),
            contentScale = ContentScale.Fit
        )

        // Konten Utama
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Bagian Log Out (Tetap Sama)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selamat Datang!",
                    style = MaterialTheme.typography.titleLarge
                )
                TextButton(onClick = {
                    logoutUser(navController)
                }) {
                    Text("Log Out")
                }
            }

            // --- BAGIAN MENU (Tetap Sama) ---
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Menu", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // (Tombol Redeem, Lokasi, Panduan Anda tetap sama di sini)
                // Tombol Redeem
                Button(
                    onClick = { navController.navigate("redeem") },
                    colors = ButtonDefaults.buttonColors(containerColor = darkGreenColor, contentColor = Color.White),
                    modifier = Modifier.weight(1f).height(100.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_reedem_point),
                            contentDescription = "Redeem Icon",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Redeem")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Tombol Lokasi
                Button(
                    onClick = { navController.navigate("location") },
                    colors = ButtonDefaults.buttonColors(containerColor = darkGreenColor, contentColor = Color.White),
                    modifier = Modifier.weight(1f).height(100.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_lokasi),
                            contentDescription = "Location Icon",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Lokasi")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Tombol Panduan
                Button(
                    onClick = { navController.navigate("guide") },
                    colors = ButtonDefaults.buttonColors(containerColor = darkGreenColor, contentColor = Color.White),
                    modifier = Modifier.weight(1f).height(100.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.icon_panduan),
                            contentDescription = "Guide Icon",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Panduan")
                    }
                }
            }
            // --- -------------------------------------------------- ---

            // --- 👇 "HISTORY TERAKHIR" (BAGIAN BARU) 👇 ---
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "History Terakhir", style = MaterialTheme.typography.titleLarge) // JUDUL BARU
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(latestHistory) { item -> // Gunakan data history
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Kolom untuk Nama dan Kuantitas
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${item.quantity} (${item.date})", // Kuantitas + Tanggal
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            // Kolom untuk Harga
                            Text(
                                text = item.price, // Harga
                                style = MaterialTheme.typography.bodyLarge,
                                color = darkGreenColor, // Warna hijau
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}