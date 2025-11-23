package com.example.trash2cash

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.navigation.NavController


fun loginUser(email: String, password: String, context: Context, onSuccess: () -> Unit) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Login Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

fun registerUser(email: String, password: String, name: String, context: Context, onSuccess: () -> Unit) {
    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    saveUserData(userId, name, email, context, onSuccess)
                }
            } else {
                Toast.makeText(context, "Registrasi Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

fun saveUserData(userId: String, name: String, email: String, context: Context, onSuccess: () -> Unit) {
    val user = User(userId = userId, name = name, email = email, points = 0L)
    FirebaseDatabase.getInstance().reference.child("users").child(userId).setValue(user)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "User Data Saved", Toast.LENGTH_SHORT).show()
                onSuccess()
            } else {
                Toast.makeText(context, "Failed to Save User Data", Toast.LENGTH_SHORT).show()
            }
        }
}

fun loadRedeemCatalog(onResult: (List<RedeemItem>) -> Unit) {
    val catalogRef = FirebaseDatabase.getInstance().reference.child("redeem_catalog")
    catalogRef.get().addOnSuccessListener {
        val catalog = it.children.mapNotNull { snapshot ->
            snapshot.getValue(RedeemItem::class.java)
        }
        onResult(catalog)
    }.addOnFailureListener {
        onResult(emptyList())
    }
}
fun addPoints(userId: String, pointsToAdd: Int, context: android.content.Context) {
    val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
    userRef.child("points").get().addOnSuccessListener { dataSnapshot ->
        val currentPoints = dataSnapshot.getValue(Long::class.java) ?: 0L
        val newPoints = currentPoints + pointsToAdd

        userRef.child("points").setValue(newPoints)
            .addOnFailureListener {
                Toast.makeText(context, "Gagal update poin.", Toast.LENGTH_SHORT).show()
            }
    }.addOnFailureListener {
        Toast.makeText(context, "Gagal mengambil poin saat ini.", Toast.LENGTH_SHORT).show()
    }
}
fun saveTransaction(userId: String, transaction: Transaction, context: android.content.Context) {
    val transactionRef = FirebaseDatabase.getInstance().reference
        .child("transactions").child(userId).push()

    transactionRef.setValue(transaction)
        .addOnSuccessListener {
            Toast.makeText(context, "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show()
            addPoints(userId, transaction.pointsEarned, context)
        }
        .addOnFailureListener {
            Toast.makeText(context, "Gagal menyimpan transaksi.", Toast.LENGTH_SHORT).show()
        }
}

fun loadTransactions(userId: String, onResult: (List<Transaction>) -> Unit) {
    val transactionRef = FirebaseDatabase.getInstance().reference.child("transactions").child(userId)

    transactionRef.get().addOnSuccessListener { dataSnapshot ->
        val transactions = dataSnapshot.children.mapNotNull { snapshot ->
            snapshot.getValue(Transaction::class.java)
        }
        onResult(transactions.reversed())
    }.addOnFailureListener {
        onResult(emptyList())
    }
}

fun logoutUser(navController: NavController) {
    FirebaseAuth.getInstance().signOut()
    navController.navigate("login") {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }
}