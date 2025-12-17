package com.example.trash2cash.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trash2cash.R
import com.example.trash2cash.logoutUser
import com.example.trash2cash.Transaction
import com.example.trash2cash.loadTransactions
import com.google.firebase.auth.FirebaseAuth
import com.example.trash2cash.ui.components.MenuButton

@Composable
fun HomeScreen(navController: NavController) {

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF40E0D0), Color(0xFF3CB371))
    )

    val auth = FirebaseAuth.getInstance()
    var userId by rememberSaveable { mutableStateOf<String?>(null) }
    var history by remember { mutableStateOf<List<Transaction>>(emptyList()) }

    LaunchedEffect(Unit) {
        userId = auth.currentUser?.uid
    }

    val refreshState =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("refresh_history", false)
            ?.collectAsState()

    fun loadHistory() {
        userId?.let {
            loadTransactions(it) { result ->
                history = result
            }
        }
    }

    // Pertama kali masuk Home
    LaunchedEffect(Unit) {
        loadHistory()
    }

    // Saat kembali dari Redeem
    LaunchedEffect(refreshState?.value) {
        if (refreshState?.value == true) {
            loadHistory()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("refresh_history", false)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(Modifier.height(24.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selamat Datang!",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                TextButton(onClick = { logoutUser(navController) }) {
                    Text("Log Out", color = Color.White)
                }
            }

            Spacer(Modifier.height(24.dp))

            // Menu
            Text("Menu", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MenuButton(navController, "redeem", R.drawable.icon_reedem_point, "Redeem")
                MenuButton(navController, "location", R.drawable.icon_lokasi, "Lokasi")
                MenuButton(navController, "guide", R.drawable.icon_panduan, "Panduan")
                MenuButton(navController, "scan", R.drawable.icon_scan, "Scan")
            }

            Spacer(Modifier.height(24.dp))

            // History
            Text(
                text = "History Terakhir",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (history.isEmpty()) {
                    item {
                        Text(
                            text = "Belum ada transaksi.",
                            color = Color.White
                        )
                    }
                } else {
                    items(history) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = item.wasteType,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Jumlah: ${item.quantity} ${item.unit}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Text(
                                    text = "+ ${item.pointsEarned} poin",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF3CB371)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
