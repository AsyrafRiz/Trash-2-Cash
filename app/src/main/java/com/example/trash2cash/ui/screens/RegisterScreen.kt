package com.example.trash2cash.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trash2cash.registerUser

@Composable
fun RegisterScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Registrasi Pengguna", style = MaterialTheme.typography.titleLarge)
        // ... (Spacer dan TextField untuk Email, Password, Name seperti sebelumnya) ...
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = name, onValueChange = { name = it }, label = { Text("Nama") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (email.isNotBlank() && password.isNotBlank() && name.isNotBlank()) {
                registerUser(email, password, name, context) {
                    // Ini adalah onSuccess callback! Navigasi ke home.
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true } // Hapus login/register dari back stack
                    }
                }
            } else {
                Toast.makeText(context, "Isi semua field", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Register")
        }
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Sudah punya akun? Login")
        }
    }
}