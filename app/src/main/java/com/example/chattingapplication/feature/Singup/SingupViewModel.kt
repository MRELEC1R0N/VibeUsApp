package com.example.chattingapplication.feature.Singup

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state = _state.asStateFlow()

    fun signUp(name:String, email: String, password: String) {
        _state.value = SignUpState.Loading
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let { firebaseUser ->
                        val user = User(
                            userId = firebaseUser.uid,
                            username = name,
                            email = email
                        )
                        val db = FirebaseFirestore.getInstance()
                        db.collection("users").document(user.userId).set(user)
                            .addOnSuccessListener {
                                firebaseUser.updateProfile(
                                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build()
                                )
                                _state.value = SignUpState.Success
                            }
                            .addOnFailureListener {
                                _state.value = SignUpState.Error
                            }
                    }
                } else {
                    _state.value = SignUpState.Error
                }
            }
    }
}
sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()
}