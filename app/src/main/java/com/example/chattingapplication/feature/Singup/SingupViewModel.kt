package com.example.chattingapplication.feature.Singup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.chattingapplication.UserSignup as User

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(username: String, email: String, password: String) {
        _state.value = SignUpState.Loading
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { firebaseUser ->
                        val user = User(
                            userId = firebaseUser.uid,
                            username = username,
                            email = email
                        )
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(user.userId).set(user)
                            .addOnSuccessListener {
                                firebaseUser.updateProfile(
                                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
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

sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    data class Error(val message: String) : SignUpState()
}
