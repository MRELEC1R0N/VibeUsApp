package com.example.chattingapplication.feature.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.viewmodel.compose.viewModel



//@Composable
//fun NotificationListScreen(
//    navController: NavController,
//    viewModel: NotificationsViewModel = viewModel(),
//    currentUserId: String
//) {
//    val notifications by viewModel.notifications.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
//
//    LaunchedEffect(currentUserId) {
//        viewModel.fetchNotifications(currentUserId)
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)
//            .padding(16.dp)
//    ) {
//        Column {
//            Text(
//                text = "Friend Requests",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier
//                    .padding(bottom = 16.dp)
//                    .align(Alignment.CenterHorizontally)
//            )
//
//            if (errorMessage != null) {
//                Text(
//                    text = errorMessage ?: "",
//                    color = Color.Red,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            } else if (notifications.isEmpty()) {
//                Text(
//                    text = "No new requests",
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            } else {
//
//            }
//        }
//    }
//}
