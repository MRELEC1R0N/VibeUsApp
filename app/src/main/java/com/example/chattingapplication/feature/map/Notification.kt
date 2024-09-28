package com.example.chattingapplication.feature.map

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.chattingapplication.FriendRequest
import com.example.chattingapplication.feature.navigation.NavigationRouts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun NotificationBellWithRequests() {
    var showRequests by remember { mutableStateOf(false) }
    var friendRequests by remember { mutableStateOf<List<FriendRequest>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Map Screen") },
                actions = {
                    IconButton(onClick = {
                        if (currentUserId != null) {
                            fetchFriendRequestsWithUserInfo(currentUserId) { requests, error ->
                                if (error != null) {
                                    errorMessage = error
                                } else {
                                    friendRequests = requests
                                    showRequests = true
                                }
                            }
                        }
                    }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        }
    ) {
        if (showRequests) {
            if (friendRequests.isNotEmpty()) {
                FriendRequestsCard(friendRequests = friendRequests, onDismiss = { showRequests = false })
            } else if (errorMessage != null) {
                ErrorCard(message = errorMessage!!)
            } else {
                Text(text = "No new friend requests." , modifier = Modifier.padding(it))
            }
        }
    }
}



fun fetchFriendRequestsWithUserInfo(userId: String, onResult: (List<FriendRequest>, String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val friendRequests = mutableListOf<FriendRequest>()

    db.collection("users").document(userId).collection("requests")
        .get()
        .addOnSuccessListener { requestDocuments ->
            val userRequests = requestDocuments.documents

            // For each request, fetch the user details
            userRequests.forEach { requestDoc ->
                val fromUserId = requestDoc.getString("fromUserId") ?: return@forEach

                // Fetch the user details of the sender
                db.collection("users").document(fromUserId).get()
                    .addOnSuccessListener { userDoc ->
                        val username = userDoc.getString("username") ?: "Unknown User"
                        val age = (userDoc.getLong("age")?.toInt() ?: 0).toString()
                        val profileImageUrl = userDoc.getString("profileImageUrl") ?: ""

                        // Add the user's details to the friend request list
                        friendRequests.add(
                            FriendRequest(
                                fromUserId = fromUserId,
                                username = username,
                                age = age,
                                profileImageUrl = profileImageUrl
                            )
                        )

                        // If all requests have been processed, return the result
                        if (friendRequests.size == userRequests.size) {
                            onResult(friendRequests, null)
                        }
                    }
                    .addOnFailureListener { exception ->
                        onResult(emptyList(), "Error fetching user info: ${exception.message}")
                    }
            }
        }
        .addOnFailureListener { exception ->
            onResult(emptyList(), "Error fetching friend requests: ${exception.message}")
        }
}




@Composable
fun FriendRequestsCard(
    friendRequests: List<FriendRequest>,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Friend Requests", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                LazyColumn {
                    items(friendRequests) { request ->
                        FriendRequestItem(request)
                    }
                }

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
fun FriendRequestItem(friendRequest: FriendRequest) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(friendRequest.profileImageUrl),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = friendRequest.username, fontWeight = FontWeight.Bold)
            Text(text = "Age: ${friendRequest.age}")
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { /* Handle accept request */ }) {
            Text(text = "Accept")
        }
    }
}


@Composable
fun ErrorCard(message: String) {
    Card(modifier = Modifier.padding(16.dp)) {
        Text(text = "Error: $message", color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}




//@Composable
//fun NotificationListScreen(
//    navController: NavController,
//    currentUserId: String
//) {
//    val db = FirebaseFirestore.getInstance()
//    val requestList = remember { mutableStateListOf<Map<String, Any>>() }
//    val loading = remember { mutableStateOf(true) }  // Track loading state
//
//    // Fetching friend requests
//    LaunchedEffect(currentUserId) {
//        db.collection("users")
//            .document(currentUserId)
//            .collection("requests")
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val request = document.data
//                    requestList.add(request)
//                }
//                loading.value = false // Loading completed
//            }
//            .addOnFailureListener {
//                loading.value = false // Stop loading even if there's an error
//            }
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
//            when {
//                loading.value -> {
//                    // Display loading indicator
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//                }
//                requestList.isEmpty() -> {
//                    // Display no requests message
//                    Text(
//                        text = "No new requests",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = Modifier.align(Alignment.CenterHorizontally)
//                    )
//                }
//                else -> {
//                    // Display the list of requests
//                    LazyColumn {
//                        items(requestList) { request ->
//                            val fromUserId = request["fromUserId"] as? String ?: ""
//                            FriendRequestCard(
//                                fromUserId = fromUserId,
//                                navController = navController,
//                                requestId = request["requestId"] as? String ?: ""
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FriendRequestCard(
//    fromUserId: String,
//    navController: NavController,
//    requestId: String
//) {
//    val db = FirebaseFirestore.getInstance()
//    var fromUserName by remember { mutableStateOf("Loading...") } // Loading text while fetching name
//
//    // Fetching user details from Firestore
//    LaunchedEffect(fromUserId) {
//        db.collection("users").document(fromUserId)
//            .get()
//            .addOnSuccessListener { document ->
//                fromUserName = document.getString("username") ?: "Unknown User"
//            }
//            .addOnFailureListener {
//                fromUserName = "Error fetching name"
//            }
//    }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .clickable {
//                navController.navigate("user_profile/$fromUserId/$requestId")
//
//            },
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = fromUserName, // Now displaying the user's name
//                style = MaterialTheme.typography.bodyLarge
//            )
//            Icon(
//                imageVector = Icons.Default.ArrowForward,
//                contentDescription = "Go to Profile"
//            )
//        }
//    }
//}
