package com.example.trash2cash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trash2cash.ui.screens.*
import com.example.trash2cash.utils.createNotificationChannel
import com.example.trash2cash.utils.createNotificationChannel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        createNotificationChannel(this)

        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
                    "home"
                } else {
                    "login"
                }
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") {
                        LoginScreen(navController)
                    }
                    composable("register") {
                        RegisterScreen(navController)
                    }
                    composable("home") {
                        HomeScreen(navController)
                    }
                    composable("redeem") {
                        RedeemScreen(navController)
                    }
                    composable("location") {
                        LocationScreen(navController)
                    }
                    composable("guide") {
                        GuideScreen(navController)
                    }
                }
            }
        }
    }
}