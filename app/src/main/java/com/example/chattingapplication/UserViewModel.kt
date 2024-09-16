package com.example.chattingapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.example.chattingapplication.User

class UserViewModel : ViewModel() {

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun getUserById(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    try {
                        _user.value = User(
                            userId = document.id,
                            username = document.getString("username") ?: "",
                            email = document.getString("email") ?: ""
                        )
                    } catch (e: Exception) {
                        // Handle error parsing user data
                        _user.value = null
                    }
                } else {
                    // Handle case where user document doesn't exist
                    _user.value = null
                }
            }
            .addOnFailureListener { exception ->
                // Handle error fetching user data
                _user.value = null
            }
    }
}