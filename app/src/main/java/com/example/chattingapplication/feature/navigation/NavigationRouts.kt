package com.example.chattingapplication.feature.navigation


sealed class NavigationRouts(val route: String) {
    object LoginScreen : NavigationRouts("login")
    object SignupScreen : NavigationRouts("signup")
    object HomeScreen : NavigationRouts("home")
    object MapScreen : NavigationRouts("map")
    object ChatScreen : NavigationRouts("users")
    object AboutMeScreen : NavigationRouts("aboutme")
    object FriendRequestsScreen : NavigationRouts("friend_requests") // Add this line for Friend Requests
    object RequestUserProfile : NavigationRouts("request_user_profile/{userId}") {
        fun createRoute(userId: String) = "request_user_profile/$userId"
    }

    object UserProfile : NavigationRouts("user_profile/{userId}/{requestId}") {
        fun passUserAndRequest(userId: String, requestId: String): String {
            return "user_profile/$userId/$requestId"
        }
    }

    object PostUserScreen: NavigationRouts("post_user_profile/{userId}") {
        fun passUserId(userId: String): String {
            return "post_user_profile/$userId"
        }
    }


    // User Info Screen
    object UserInfoScreen : NavigationRouts("user_info_screen/{userId}") {
        fun passUserId(userId: String): String {
            return "user_info_screen/$userId"
        }
    }

    // Message Screen
    object MessageScreen : NavigationRouts("message_screen/{userId}") {
        fun passUserId(userId: String): String {
            return "message_screen/$userId"
        }
    }

    // Basic Info Screen
    object BasicInfoScreen :
        NavigationRouts("basic_info_screen?username={username}&email={email}") {
        fun passUserInfo(username: String, email: String): String {
            return "basic_info_screen?username=$username&email=$email"
        }
    }

}