package com.example.chattingapplication.feature.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.chattingapplication.R
import com.example.chattingapplication.feature.navigation.MyBottomNavigation
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.chattingapplication.User


@Composable
fun MapScreen(navController: NavHostController) {
    var showUserList by remember { mutableStateOf(false) }
    var users by remember { mutableStateOf<List<User>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    if (showUserList) {
        Dialog(onDismissRequest = { showUserList = false }) {
            if (errorMessage != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }} else {
                UserListCard(
                    users,
                    onClose = { showUserList = false },
                    navController = navController)
            }
        }
    }

    Scaffold(
        bottomBar = { MyBottomNavigation(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.googlemap),
                contentDescription = "Map",
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .padding(16.dp)
                    .align(Alignment.BottomEnd)
                    .background(Color.LightGray, shape = CircleShape)
            ) {
                Card(
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.Center).clickable {
                            showUserList = true
                            val db = FirebaseFirestore.getInstance()
                            db.collection("users")
                                .get()
                                .addOnSuccessListener { result ->
                                    users = result.documents.mapNotNull { document ->
                                        try {
                                           User(
                                                userId = document.id,
                                                username = document.getString("username") ?: "",
                                                email = document.getString("email") ?: ""
                                            )
                                        } catch (e: Exception) {
                                            errorMessage = "Error parsing user data"
                                            null
                                        }
                                    }.filter { it.userId != currentUserId }
                                }
                                .addOnFailureListener { exception ->
                                    errorMessage = "Error fetching users: ${exception.message}"
                                }
                        },
                    shape = RoundedCornerShape(4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "+",
                            modifier = Modifier.align(Alignment.Center),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

