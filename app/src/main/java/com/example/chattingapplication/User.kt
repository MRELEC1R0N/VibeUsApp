package com.example.chattingapplication

data class User(
    val userId: String,
    val username: String,
    val email: String,
    val fullname: String? = null,
    val age: String? = null,
    val gender: String? = null,
    val pronouns: String? = null, // Added pronouns field
    val work: String? = null, // Added work field
    val college: String? = null, // Added college field
    val location: String? = null,
    val languagesKnown: List<String> = emptyList(), // Added list for languages
    val aboutMe: String? = null,
    val datingIntention: String? = null, // Added dating intention
    val religiousBeliefs: String? = null, // Added religious beliefs
    val height: String? = null, // Added height
    val drinking: String? = null, // Added drinking habit
    val smoking: String? = null, // Added smoking habit
    val favoriteMovies: List<String> = emptyList(), // For 'My binge list'
    val favoriteArtists: List<String> = emptyList(), // For 'My playlist'
    val writtenPrompts: List<Prompt> = emptyList(), // Added list for written prompts
    val profileImageUrl: String // Profile image
)

data class Prompt(
    val question: String,
    val answer: String
)


data class UserData(
    val name: String? = null,
    val gender: String? = null,
    val pronouns: String? = null,
    val work: String? = null,
    val college: String? = null,
    val hometown: String? = null,
    val languages: String? = null,
    val datingIntention: String? = null,
    val religiousBeliefs: String? = null,
    val height: String? = null,
    val drinking: String? = null,
    val smoking: String? = null
)



data class FriendRequest(
    val fromUserId: String = "",
    val username: String = "",
    val age: String = "",
    val profileImageUrl: String = ""
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