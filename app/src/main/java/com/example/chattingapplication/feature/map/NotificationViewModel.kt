package com.example.chattingapplication.feature.map

//import android.util.Log
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.chattingapplication.RequestNotification
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await
//
//class NotificationsViewModel : ViewModel() {
//
//    private val _notifications = MutableStateFlow<List<RequestNotification>>(emptyList()) // Using StateFlow
//    val notifications: StateFlow<List<RequestNotification>> = _notifications
//
//    private val _errorMessage = MutableStateFlow<String?>(null) // Error message flow
//    val errorMessage: StateFlow<String?> = _errorMessage
//
//    fun fetchNotifications(userId: String) {
//        viewModelScope.launch {
//            try {
//                val db = FirebaseFirestore.getInstance()
//                val result = db.collection("notifications")
//                    .whereEqualTo("toUserId", userId)
//                    .get()
//                    .await() // Use await() to make Firestore call suspendable
//
//                val notificationsList = result.documents.mapNotNull { document ->
//                    document.toObject(RequestNotification::class.java)
//                }
//
//                _notifications.value = notificationsList
//            } catch (e: Exception) {
//                Log.e("NotificationsViewModel", "Error fetching notifications: ", e)
//                _errorMessage.value = "Failed to fetch notifications: ${e.message}" // Expose error message to the UI
//            }
//        }
//    }
//}
//
