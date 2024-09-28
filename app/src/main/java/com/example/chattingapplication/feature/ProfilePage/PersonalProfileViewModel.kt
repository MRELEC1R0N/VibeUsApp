package com.example.chattingapplication.feature.ProfilePage

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chattingapplication.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonalProfileViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData?>()
    val userData: MutableLiveData<UserData?> get() = _userData

    fun fetchUserData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId).addSnapshotListener { documentSnapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {
                val user = documentSnapshot.toObject(UserData::class.java)
                _userData.value = user
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
}
