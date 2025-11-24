package com.example.trash2cash
data class User(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val points: Long = 0L
)
data class RedeemItem(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val pointsRequired: Long = 0L,
    val imageUrl: String = ""
)
data class Transaction(
    val wasteType: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val pointsEarned: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)