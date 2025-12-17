package com.example.trash2cash

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

/* ===================== AUTH ===================== */

fun loginUser(
    email: String,
    password: String,
    context: Context,
    onSuccess: () -> Unit
) {
    FirebaseAuth.getInstance()
        .signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(
                    context,
                    "Login Gagal: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

fun registerUser(
    email: String,
    password: String,
    name: String,
    context: Context,
    onSuccess: () -> Unit
) {
    FirebaseAuth.getInstance()
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    saveUserData(userId, name, email, context, onSuccess)
                }
            } else {
                Toast.makeText(
                    context,
                    "Registrasi Gagal: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

fun saveUserData(
    userId: String,
    name: String,
    email: String,
    context: Context,
    onSuccess: () -> Unit
) {
    val user = User(
        userId = userId,
        name = name,
        email = email,
        points = 0L
    )

    FirebaseDatabase.getInstance()
        .reference
        .child("users")
        .child(userId)
        .setValue(user)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "User Data Saved", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Failed to Save User Data", Toast.LENGTH_SHORT).show()
            }
        }
}

/* ===================== REDEEM ===================== */

fun loadRedeemCatalog(onResult: (List<RedeemItem>) -> Unit) {
    FirebaseDatabase.getInstance()
        .reference
        .child("redeem_catalog")
        .get()
        .addOnSuccessListener { snapshot ->
            val catalog = snapshot.children.mapNotNull {
                it.getValue(RedeemItem::class.java)
            }
            onResult(catalog)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}

/* ===================== POINTS ===================== */

fun addPoints(
    userId: String,
    pointsToAdd: Int,
    context: Context
) {
    val userRef = FirebaseDatabase.getInstance()
        .reference
        .child("users")
        .child(userId)

    userRef.child("points")
        .get()
        .addOnSuccessListener { snapshot ->
            val currentPoints = snapshot.getValue(Long::class.java) ?: 0L
            val newPoints = currentPoints + pointsToAdd

            userRef.child("points").setValue(newPoints)
                .addOnFailureListener {
                    Toast.makeText(context, "Gagal update poin.", Toast.LENGTH_SHORT).show()
                }
        }
        .addOnFailureListener {
            Toast.makeText(context, "Gagal mengambil poin.", Toast.LENGTH_SHORT).show()
        }
}

/* ===================== TRANSACTIONS ===================== */

fun saveTransaction(
    userId: String,
    transaction: Transaction,
    context: Context
) {
    val transactionRef = FirebaseDatabase.getInstance()
        .reference
        .child("transactions")
        .child(userId)
        .push()

    transactionRef
        .setValue(transaction)
        .addOnSuccessListener {
            addPoints(userId, transaction.pointsEarned, context)
            Toast.makeText(context, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Gagal menyimpan transaksi.", Toast.LENGTH_SHORT).show()
        }
}

fun loadTransactions(
    userId: String,
    onResult: (List<Transaction>) -> Unit
) {
    val ref = FirebaseDatabase.getInstance()
        .reference
        .child("transactions")
        .child(userId)

    ref.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
            val list = snapshot.children.mapNotNull {
                it.getValue(Transaction::class.java)
            }
            onResult(list.reversed())
        }

        override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
            onResult(emptyList())
        }
    })
}

/* ===================== LOGOUT ===================== */

fun logoutUser(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.navigate("login") {
        popUpTo(0) { inclusive = true }
        launchSingleTop = true
    }
}
