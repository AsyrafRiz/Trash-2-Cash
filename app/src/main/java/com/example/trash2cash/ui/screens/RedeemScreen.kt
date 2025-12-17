package com.example.trash2cash.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trash2cash.Transaction
import com.example.trash2cash.saveTransaction
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(navController: NavController) {

    val context = LocalContext.current

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val wasteTypes = listOf(
        "Organik" to 1000L,
        "Plastik" to 2000L,
        "Kaca" to 20000L,
        "Logam" to 40000L
    )

    var expanded by remember { mutableStateOf(false) }

    var selectedWaste by remember { mutableStateOf<Pair<String, Long>?>(null) }

    var quantity by remember { mutableStateOf("") }

    var quantityError by remember { mutableStateOf<String?>(null) }

    val totalPoints: Long = selectedWaste?.second?.times(quantity.toLongOrNull() ?: 0L) ?: 0L

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Proses Sampah") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
                        .fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.textFieldColors()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    wasteTypes.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item.first) },
                            onClick = {
                                selectedWaste = item
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            TextField(
                value = quantity,
                onValueChange = { 
                    quantity = it.filter { char -> char.isDigit() }
                    quantityError = null 
                },
                label = { Text("Jumlah Sampah") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = quantityError != null,
                supportingText = {
                    if (quantityError != null) {
                        Text(quantityError!!)
                    }
                }
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
                    val currentQuantity = quantity.toDoubleOrNull()
                    if (currentQuantity == null || currentQuantity <= 0) {
                        quantityError = "Jumlah harus lebih dari 0"
                        return@Button
                    }
                    
                    if (totalPoints > Int.MAX_VALUE) {
                        Toast.makeText(context, "Jumlah poin yang dimasukkan terlalu besar!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val transaction = Transaction(
                        wasteType = selectedWaste!!.first,
                        quantity = currentQuantity,
                        unit = "unit",
                        pointsEarned = totalPoints.toInt()
                    )

                    saveTransaction(userId, transaction, context)
                    navController.popBackStack()
                }
            ) {
                Text("Simpan Transaksi")
            }
        }
    }
}