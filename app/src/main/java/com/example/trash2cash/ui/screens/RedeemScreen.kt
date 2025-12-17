package com.example.trash2cash.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.trash2cash.Transaction
import com.example.trash2cash.saveTransaction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(navController: NavController) {

    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val wasteTypes = listOf(
        "Organik" to 1000,
        "Plastik" to 2000,
        "Kaca" to 20000,
        "Logam" to 40000
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedWaste by remember { mutableStateOf<Pair<String, Int>?>(null) }
    var quantity by remember { mutableStateOf("") }

    val totalPoints =
        selectedWaste?.second?.times(quantity.toIntOrNull() ?: 0) ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proses Sampah") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedWaste?.first ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jenis Sampah") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    wasteTypes.forEach {
                        DropdownMenuItem(
                            text = { Text(it.first) },
                            onClick = {
                                selectedWaste = it
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            TextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Jumlah Sampah") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Total Poin: $totalPoints",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(24.dp))

            Button(
                enabled = selectedWaste != null && quantity.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val transaction = Transaction(
                        wasteType = selectedWaste!!.first,
                        quantity = quantity.toDouble(),
                        unit = "unit",
                        pointsEarned = totalPoints
                    )

                    // SIMPAN KE FIREBASE
                    saveTransaction(userId, transaction, context)

                    // LANGSUNG KEMBALI KE HOME
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            ) {
                Text("Simpan Transaksi")
            }
        }
    }
}
