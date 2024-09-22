package com.example.chattingapplication

data class UserSignup(
    val userId: String,
    val username: String,
    val email: String,
    val fullname: String? = null,
    val age: String? = null,
    val gender: String? = null,
    val location: String? = null,
    val aboutMe: String? = null,
    val favoriteMovies: List<String> = emptyList()

)


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

data class Movie(
    val title: String,
    val posterUrl: String
)