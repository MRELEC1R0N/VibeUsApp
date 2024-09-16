package com.example.chattingapplication.feature.navigation


sealed class NavigationRouts(val route: String) {
    object LoginScreen : NavigationRouts("login")
    object SignupScreen: NavigationRouts("signup")
    object HomeScreen : NavigationRouts("home")
    object MapScreen : NavigationRouts("map")
    object ChatScreen : NavigationRouts("users")
    object UserInfoScreen : NavigationRouts("user_info_screen/{userId}") {
        fun passUserId(userId: String): String {
            return "user_info_screen/$userId"
        }
    }
    object MessageScreen : NavigationRouts("message_screen/{userId}") {
        fun passUserId(userId: String): String {
            return "message_screen/$userId"
        }
    }
}