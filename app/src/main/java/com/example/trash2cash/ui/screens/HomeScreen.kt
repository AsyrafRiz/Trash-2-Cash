package com.example.trash2cash.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.trash2cash.R
import com.example.trash2cash.ScanHistory
import com.example.trash2cash.logoutUser
import com.example.trash2cash.ui.components.MenuButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(navController: NavController) {

    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF40E0D0), Color(0xFF3CB371))
    )

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

            Spacer(Modifier.height(16.dp))

            // Total Cash Display
            TotalCashDisplay()

            Spacer(Modifier.height(24.dp))

            // Menu
            Text("Menu", style = MaterialTheme.typography.titleMedium, color = Color.White)
            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

            HistoryList()
        }
    }
}

@Composable
fun TotalCashDisplay() {
    var totalCash by remember { mutableStateOf(0L) }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    if (uid != null) {
        val db = Firebase.firestore
        val userDocRef = db.collection("users").document(uid)

        DisposableEffect(uid) {
            val listener = userDocRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    totalCash = snapshot.getLong("totalCash") ?: 0L
                }
            }
            onDispose {
                listener.remove()
            }
        }
    }

    val formattedCash = NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(totalCash)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Total Cash Anda", color = Color.White, fontSize = 16.sp)
            Text(formattedCash, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun HistoryList() {
    var history by remember { mutableStateOf<List<ScanHistory>>(emptyList()) }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    if (uid != null) {
        val db = Firebase.firestore
        val historyCollectionRef = db.collection("users").document(uid).collection("history")

        DisposableEffect(uid) {
            val listener = historyCollectionRef.orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        history = snapshot.toObjects(ScanHistory::class.java)
                    }
                }
            onDispose {
                listener.remove()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (history.isEmpty()) {
            item {
                Text("Belum ada riwayat transaksi.", color = Color.White)
            }
        } else {
            items(history) { item ->
                val formattedAmount =
                    NumberFormat.getCurrencyInstance(Locale("id", "ID")).format(item.amount)
                val formattedDate = item.timestamp?.let {
                    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(it)
                } ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.jenisSampah, fontWeight = FontWeight.Bold)
                            Text(
                                formattedDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Text(
                            formattedAmount,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3CB371)
                        )
                    }
                }
            }
        }
    }
}