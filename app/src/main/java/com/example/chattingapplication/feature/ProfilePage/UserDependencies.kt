package com.example.chattingapplication.feature.ProfilePage

import android.os.Build
import android.service.autofill.UserData
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val firestore = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.P)
    fun getUserData(userId: String, onResult: (UserData?) -> Unit) {
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userData = document.toObject(UserData::class.java)
                    onResult(userData)
                } else {
                    onResult(null) // Document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
                onResult(null)
            }
    }
}
