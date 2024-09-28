package com.example.chattingapplication.feature.ProfilePage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen

@Composable
fun RequestUserProfile(
    userId: String,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    if (currentUserId == null) {
        // Handle user not authenticated state
        Text("User not authenticated")
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
        ) {
            // Use the existing UserInfoScreen
            UserInfoScreen(
                userId = userId,
                onBackClick = onBackClick,
                navController = navController
            )
        }

        // Card for 2 circular buttons (Cross, Tick) positioned at the bottom of the screen
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Cross Button
                IconButton(
                    onClick = {
                        onRequestDeclined(userId, currentUserId, navController) // Decline request
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear, // Cross symbol
                        contentDescription = "Decline",
                        tint = Color.White
                    )
                }

                // Tick Button
                IconButton(
                    onClick = {
                        onRequestAccepted(userId, currentUserId, navController) // Accept request
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check, // Tick symbol
                        contentDescription = "Accept",
                        tint = Color.White
                    )
                }
            }
        }
    }
}


fun onRequestAccepted(
    userId: String,
    currentUserId: String,
    navController: NavController
) {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Remove the friend request from the Firestore
    firestore.collection("users").document(currentUserId).collection("requests").document(userId)
        .delete()
        .addOnSuccessListener {
            // Navigate to the message screen with the accepted userId
            navController.navigate("message_screen/$userId") // Ensure to replace with your actual message screen route
        }
        .addOnFailureListener { e ->
            // Handle the error appropriately
            println("Error accepting request: ${e.message}")
        }
}


fun onRequestDeclined(
    userId: String,
    currentUserId: String,
    navController: NavController
) {
    val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Remove the friend request from Firestore
    firestore.collection("users").document(currentUserId).collection("requests").document(userId)
        .delete()
        .addOnSuccessListener {
            // Navigate back to the map screen
            navController.popBackStack() // Go back to the previous screen (Map screen)
        }
        .addOnFailureListener { e ->
            // Handle the error appropriately
            println("Error declining request: ${e.message}")
        }
}
