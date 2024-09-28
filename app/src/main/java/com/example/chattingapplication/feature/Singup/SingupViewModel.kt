package com.example.chattingapplication.feature.Singup

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.chattingapplication.User as User

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(username: String, email: String, password: String) {
        _state.value = SignUpState.Loading

        // Fetch random profile image from Firebase Storage
        fetchRandomProfileImage { profileImageUrl ->
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.user?.let { firebaseUser ->
                            val user = User(
                                userId = firebaseUser.uid,
                                username = username,
                                email = email,
                                profileImageUrl = profileImageUrl // Set random profile image URL
                            )
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users").document(user.userId).set(user)
                                .addOnSuccessListener {
                                    firebaseUser.updateProfile(
                                        com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)
                                            .setPhotoUri(Uri.parse(profileImageUrl)) // Set profile image in Firebase Auth
                                            .build()
                                    )
                                    _state.value = SignUpState.Success
                                }
                                .addOnFailureListener { exception ->
                                    _state.value = SignUpState.Error(exception.message ?: "Failed to save user data.")
                                }
                        }
                    } else {
                        _state.value = SignUpState.Error(task.exception?.message ?: "Sign up failed.")
                    }
                }
        }
    }

    // Fetch random profile image URL from Firebase Storage
    private fun fetchRandomProfileImage(onSuccess: (String) -> Unit) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference // Root path in Firebase Storage

        // Fetch all images in the root folder
        storageRef.listAll()
            .addOnSuccessListener { listResult ->
                val imageRefs = listResult.items
                if (imageRefs.isNotEmpty()) {
                    val randomImageRef = imageRefs.random() // Select a random image

                    // Get download URL of the random image
                    randomImageRef.downloadUrl.addOnSuccessListener { uri ->
                        onSuccess(uri.toString()) // Return the image URL
                    }.addOnFailureListener {
                        onSuccess("") // Fallback in case of failure
                    }
                } else {
                    onSuccess("") // Fallback if no images found
                }
            }
            .addOnFailureListener { exception ->
                onSuccess("") // Fallback in case of failure
            }
    }

}


sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}
