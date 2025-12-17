package com.example.trash2cash

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Date
import com.google.firebase.firestore.ServerTimestamp


data class ScanHistory(
    val jenisSampah: String = "",
    val amount: Long = 0,
    @ServerTimestamp val timestamp: Date? = null
)

fun processTrashDeposit(context: Context, onComplete: () -> Unit) {
    val garbageTypes = mapOf(
        "Plastik" to 1000L,
        "Kertas" to 500L,
        "Logam" to 2000L,
        "Kaca" to 1500L
    )

    val randomGarbage = garbageTypes.entries.random()
    val cashAmount = randomGarbage.value
    val trashType = randomGarbage.key

    val uid = FirebaseAuth.getInstance().currentUser?.uid
    if (uid == null) {
        Toast.makeText(context, "Error: Pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show()
        onComplete()
        return
    }

    val db = Firebase.firestore
    val userDocRef = db.collection("users").document(uid)
    val historyDocRef = userDocRef.collection("history").document()

    val newHistory = ScanHistory(jenisSampah = trashType, amount = cashAmount)

    db.runBatch { batch ->
        batch.update(userDocRef, "totalCash", FieldValue.increment(cashAmount))
        batch.set(historyDocRef, newHistory)
    }.addOnSuccessListener {
        val successMessage = "Selamat! Anda mendapatkan Rp$cashAmount untuk sampah $trashType."
        Toast.makeText(context, successMessage, Toast.LENGTH_LONG).show()
        onComplete()
    }.addOnFailureListener { e ->
        Toast.makeText(context, "Gagal memproses deposit: ${e.message}", Toast.LENGTH_SHORT).show()
        Log.w("Firestore", "Error processing deposit", e)
        onComplete()
    }
}


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
                    createUserProfile(userId, name)
                    onSuccess()
                }
            } else {
                Toast.makeText(context, "Registrasi Gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}

fun createUserProfile(uid: String, name: String?) {
    val db = Firebase.firestore
    val user = hashMapOf(
        "name" to name,
        "totalCash" to 0L
    )
    db.collection("users").document(uid).set(user)
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
