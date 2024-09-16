package com.example.chattingapplication

data class User(
    val userId: String,
    val username: String,
    val email: String,
    val profilePictureUrl: String? = null
)


// Message data class
data class Message(
    var senderId: String = "",
    var receiverId: String = "",
    var messageText: String = "",
    var timestamp: Long = 0L
)