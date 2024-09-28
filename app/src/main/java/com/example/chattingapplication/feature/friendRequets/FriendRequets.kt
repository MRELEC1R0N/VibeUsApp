package com.example.chattingapplication.feature.friendRequets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendRequestsScreen(navController: NavController) {
    // Get the current user's ID from Firebase Authentication
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    if (currentUserId != null) {
        // Display the friend requests
        FriendRequests(userId = currentUserId, navController = navController)
    } else {
        // Handle the case when the user is not authenticated
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("User not authenticated", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        }
    }
}

@Composable
fun FriendRequests(userId: String, navController: NavController) {
    val firestore: FirebaseFirestore = Firebase.firestore
    var friendRequests by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var userDetails by remember { mutableStateOf<Map<String, UserDetails>>(emptyMap()) }

    // Fetching friend requests from Firestore
    LaunchedEffect(userId) {
        firestore.collection("users").document(userId).collection("requests")
            .get()
            .addOnSuccessListener { snapshot ->
                val senderIds = snapshot.documents.mapNotNull { document ->
                    document.getString("fromUserId")
                }
                friendRequests = senderIds
                fetchUserDetails(senderIds, firestore) { details ->
                    userDetails = details
                    isLoading = false
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error appropriately
                isLoading = false
            }
    }

    // Loading state
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(friendRequests) { friendId ->
                userDetails[friendId]?.let { userDetail ->
                    FriendRequestItem(userDetail) {
                        // Navigate to the RequestUserProfile screen when an item is clicked
                        navController.navigate(Screen.RequestUserProfile.createRoute(friendId))
                    }
                }
            }
        }
    }
}

@Composable
fun FriendRequestItem(userDetail: UserDetails, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Load the profile image using Coil
            Image(
                painter = rememberAsyncImagePainter(userDetail.profilePicUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = userDetail.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Age: ${userDetail.age}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}

// Fetch user details for the list of friend IDs
private fun fetchUserDetails(
    friendRequests: List<String>,
    firestore: FirebaseFirestore,
    onComplete: (Map<String, UserDetails>) -> Unit
) {
    val detailsMap = mutableMapOf<String, UserDetails>()
    val fetchCount = friendRequests.size
    var completedRequests = 0

    for (friendId in friendRequests) {
        firestore.collection("users").document(friendId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val name = document.getString("username") ?: "Unknown"
                    val age = document.getLong("age")?.toInt() ?: 0
                    val profilePicUrl = document.getString("profileImageUrl") ?: ""
                    detailsMap[friendId] = UserDetails(name, age, profilePicUrl)
                }
                completedRequests++
                if (completedRequests == fetchCount) {
                    onComplete(detailsMap)
                }
            }
            .addOnFailureListener {
                completedRequests++
                if (completedRequests == fetchCount) {
                    onComplete(detailsMap)
                }
            }
    }
}

// Data class for user details
data class UserDetails(
    val name: String,
    val age: Int,
    val profilePicUrl: String
)
