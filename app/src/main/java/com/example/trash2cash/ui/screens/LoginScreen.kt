// File: com/example/trash2cash/ui/screens/LoginScreen.kt
package com.example.trash2cash.ui.screens

// --- SEMUA IMPORT YANG ANDA BUTUHKAN ADA DI SINI ---
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // <-- Untuk backgroundColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // <-- Untuk painterResource
import androidx.compose.ui.unit.dp // <-- Untuk .dp
import androidx.navigation.NavController
import com.example.trash2cash.R // <-- Untuk R.drawable
import com.example.trash2cash.loginUser
// ---------------------------------------------------

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Tentukan warna background Anda
    val backgroundColor = Color(0xFF006400) // Hijau Tua

    // 1. GUNAKAN SURFACE UNTUK MENGGANTI BACKGROUND
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor // Terapkan warna background di sini
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 2. GAMBAR LOGO ANDA (KODE YANG DIPERBAIKI)
            Image(
                // INI PERBAIKANNYA (sintaks yang benar)
                painter = painterResource(id = R.drawable.icon_bank_sampah),
                contentDescription = "Logo Aplikasi",
                // INI PERBAIKANNYA (modifier di dalam Image)
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. JUDUL
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White // Agar teks terbaca
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. TEXTFIELD
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 5. TOMBOL
            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        loginUser(email, password, context) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Isi semua field", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = { navController.navigate("register") },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Ubah warna teks agar terlihat
                Text("Belum punya akun? Registrasi", color = Color.White)
            }
        }
    }
}