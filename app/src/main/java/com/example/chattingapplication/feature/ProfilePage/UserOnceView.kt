package com.example.chattingapplication.feature.ProfilePage

import android.widget.Toast
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
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chattingapplication.feature.navigation.NavigationRouts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


@Composable
fun PostUserProfile(
    userId: String,
    navController: NavController,
    onBackClick: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(it) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp)
            ) {
                UserInfoScreen(
                    userId = userId,
                    onBackClick = onBackClick,
                    navController = navController
                )
            }

            // Card for 3 circular buttons (Cross, Heart, Tick) positioned at the bottom of the screen
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
                        onClick = { navController.navigate(NavigationRouts.MapScreen.route) },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Cross",
                            tint = Color.White
                        )
                    }

                    // Heart Button
                    IconButton(
                        onClick = { /* TODO: Handle heart action */ },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite, // Heart symbol
                            contentDescription = "Heart",
                            tint = Color.White
                        )
                    }

                    // Tick Button
                    IconButton(
                        onClick = {
                            val request = hashMapOf(
                                "fromUserId" to currentUserId,
                                "status" to "pending"
                            )
                            // Add the request to the selected user's document
                            FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(userId)  // selected user's ID
                                .collection("requests")
                                .add(request)
                                .addOnSuccessListener {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Request sent!"
                                        )
                                    }
                                }
                                .addOnFailureListener {
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(
                                            "Failed to send request"
                                        )
                                    }
                                }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check, // Tick symbol
                            contentDescription = "Tick",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostUserProfile() {
    val navController = rememberNavController()

    PostUserProfile(
        userId = "testUserId", // Sample userId
        navController = navController,
        onBackClick = { /* Sample back action */ }
    )
}
