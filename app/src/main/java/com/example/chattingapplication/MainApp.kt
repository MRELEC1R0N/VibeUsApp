package com.example.chattingapplication

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.chattingapplication.feature.ProfilePage.HomeScreen
import com.example.chattingapplication.feature.Login.LoginScreen
import com.example.chattingapplication.feature.ProfilePage.UserInfoScreen
import com.example.chattingapplication.feature.Singup.SignupScreen
import com.example.chattingapplication.feature.chat.MessageScreen
import com.example.chattingapplication.feature.chat.usersList
import com.example.chattingapplication.feature.map.MapScreen
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen



@Composable
fun MainApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
            composable(Screen.LoginScreen.route) { LoginScreen(navController) }
            composable(Screen.SignupScreen.route) { SignupScreen(navController) }
            composable(Screen.HomeScreen.route) { HomeScreen(navController) }
            composable(Screen.MapScreen.route) { MapScreen(navController ) }
            composable(Screen.ChatScreen.route) { usersList(navController) }

            composable(
                route = Screen.UserInfoScreen.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                UserInfoScreen(userId = userId, onBackClick = { navController.popBackStack() }, navController = navController)
            }
            composable(
                route = Screen.MessageScreen.route,
                arguments = listOf(
                    navArgument("userId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: ""
                MessageScreen(userId = userId, onBackClick = { navController.popBackStack() })
            }
        }
    }
}